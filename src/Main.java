import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws Exception {
		Random rmd = new Random();
		Scanner sc = new Scanner(System.in);
		int act;
		boolean next = true;

		String name;
		int score = 0;
		int gu = 9;
		int cyoki = 8;
		int pa = 8;
		int win = 0;
		int lose = 0;
		int Rwin = 0;
		int Rlose = 0;

		//もっといい記述があったような・・・
		Enemy enemys[] = new Enemy[7];
		enemys[0] = new Enemy("八我　　　　", 0); //<!>文字調整処理を時間あれば入れる
		enemys[1] = new Enemy("トクイ　　　", 1);
		enemys[2] = new Enemy("イトウ　　　", 2);
		enemys[3] = new Enemy("イワオ　　　", 3);
		enemys[4] = new Enemy("ハサミシタ　", 4);
		enemys[5] = new Enemy("カミキ　　　", 5);
		enemys[6] = new Enemy("アンドウ　　", 6);

		int enemyact = 0;
		final String[] hund = { " ", "グー", "チョキ", "パー", };
		int result; //じゃんけん判定用

		//スコア読み込み
		FileInputStream fis = new FileInputStream("point.csv");
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();
		String[] params = line.split(",");
		HighScore scores = new HighScore(params[0], Integer.parseInt(params[1]), params[2]);
		br.close();

		//開始画面
		while (next) {
			System.out.println("****消費型じゃんゲーム！～一攫千金を目指せ！****");
			System.out.println("****         1.ゲームを開始する             ****");
			System.out.println("****         2.ゲーム説明                   ****");
			System.out.println("****         3.記録の初期化                 ****");
			System.out.println("****                                        ****");
			//時間あれば名前の長さで表示調整
			System.out.printf("****最高記録：%s %05d万   %s ****%n", scores.name, scores.point, scores.day);
			System.out.println("************************************************");
			System.out.print("選択＞＞");
			act = sc.nextInt();
			switch (act) {
			case 1:
				next = false;
				break;
			case 2:
				System.out.println();
				System.out.println("～～ゲーム説明～～");
				System.out.println("このゲームではじゃんけん5回勝負を5人相手にして勝ち抜いていく");
				System.out.println("ゲームになります。");
				System.out.println("相手に勝利した場合は賞金を獲得！");
				System.out.println("じゃんけんで3回勝てば勝利となります!勝ち数に応じて");
				System.out.println("ボーナスが付きますので高額賞金を狙う方は全勝を目指してみましょう！");
				Thread.sleep(4000);
				System.out.println();
				System.out.println("～～じゃんけんの「手」について～～");
				System.out.println("このじゃんけんは消費性となり最初に");
				System.out.println("グー9枚、チョキ8枚、パー8枚が配られます。");
				System.out.println("0枚になってしまった手はもう出せませんので注意してください。");
				Thread.sleep(4000);
				System.out.println();
				System.out.println("～～対戦相手について～～");
				System.out.println("対戦相手についてはランダムに選ばれます。");
				System.out.println("対戦相手は何かしらの「手」のカードを5枚持っています。");
				System.out.println("相手によって出す手の「傾向」が違います対戦前の「情報」");
				System.out.println("を参考にしてみてください。");
				System.out.println();
				Thread.sleep(4000);
				break;
			case 3:
				System.out.println("記録を初期化します"); //おまけスコア初期化
				scores.name = "ナナシ　";
				scores.point = 0;
				scores.day = "yyyy/mm/dd";
				FileWrite(scores);
				break;
			default:
				System.out.println("1～3を選択してください");
				System.out.println();
				break;
			}
		}
		//名前入力
		while (true) {
			System.out.println("名前を入力してください(4文字)");
			name = sc.next();
			if (name.length() > 4) {
				System.out.println("名前は4文字以内にしてください。");
			} else {break;}
		}

		//本編
		System.out.printf("ようこそ%sさん  秘密のじゃんけん会場へ！%n", name);
		System.out.println("勝ちまくって稼いでいきましょう！");
		System.out.println();
		Thread.sleep(2500);
		System.out.println("さっそく今回の対戦相手を紹介しますね！");
		System.out.println();
		Thread.sleep(2000);

		//エネミーランダム
		for (int i = 2; i < enemys.length - 1; i++) {
			int index = new java.util.Random().nextInt(enemys.length - i) + i;
			Enemy tmp = enemys[index];
			enemys[index] = enemys[i];
			enemys[i] = tmp;
		}

		//ここを繰り返す
		for (int i = 1; i <= 5; i++) {
			if (i == 5) {enemys[5] = enemys[0];}
			//対戦相手発表
			Show(Rwin + Rlose, score, enemys);
			//時間あればanykey処理追加
			System.out.printf("%d回戦開始!！", i);
			System.out.printf("VS%s%n", enemys[i].Ename);
			Dialogue(enemys[i].pattern);
			System.out.println();
			next = true;
			while (next) {
				System.out.println("1:対戦開始  2:相手の情報を見る");
				System.out.print("選択＞＞");
				act = sc.nextInt();
				switch (act) {
				case 1:
					next = false;
					break;
				case 2:
					Info(enemys[i].pattern);
					System.out.println();
					break;
				default:
					System.out.println("1か2を選択してください");
					System.out.println();
					break;
				}
			}

			for (int j = 0; j < 5; j++) {
				System.out.printf("【%d回戦 %dラウンド】%n", i, j + 1);
				System.out.printf("%s 残カード：グー【%d】チョキ【%d】パー【%d】%n", name, gu, cyoki, pa);
				System.out.println();
				while (true) {
					System.out.println("どの手を出す？ 1:グー 2:チョキ 3:パー");
					System.out.print("選択＞＞");
					act = sc.nextInt();
					if (act > 3) {
						System.out.println("1～3を選択してください");
					} else if (act == 1 && gu == 0 || act == 2 && cyoki == 0 || act == 3 && pa == 0) {
						System.out.println("その「手」カードはもう無い！");
					} else if (act == 1) {
						gu--;
						break;
					} else if (act == 2) {
						cyoki--;
						break;
					} else if (act == 3) {
						pa--;
						break;
					}
				}
				//エネミーパターンによる手の選択
				enemyact = Jyan(enemys[i].pattern, j, gu, cyoki, pa);
				//じゃんけん判定
				result = (act - enemyact + 3) % 3;
				System.out.println();
				System.out.printf("あなたは%s!  相手は%s!%n", hund[act], hund[enemyact]);
				Thread.sleep(0500);
				if (result == 0) {
					System.out.println("あいこ！");
				} else if (result == 2) {
					System.out.println("あなたの勝ち！！");
					win++;
				} else {
					System.out.println("あなたの負け・・・");
					lose++;
				}
				System.out.println();
			}

			//ラウンド終了処理
			System.out.println();
			System.out.printf("%d回戦結果%n", i);
			System.out.printf("%d勝 %d負 %d引き分けで%n", win, lose, 5 - win - lose);
			if (win > lose) {
				System.out.println("あなたの勝ち！賞金300万獲得！");
				score += 300;
				Rwin++;
			} else if (win < lose) {
				System.out.println("あなたの負け　賞金300万没収させて頂きます！");
				if (Rlose == 0) {
					System.out.println("え？減るなんて聞いてない？ただで儲けられる訳");
					System.out.println("ないじゃないですかーいやだなぁハハハ");
				}
				score -= 300;
				Rlose++;
			} else {
				System.out.println("引き分けですね！賞金変動無しです");
				Rwin++; //〇回戦管理用の為
			}
			//ボーナス判定
			if (win == 5) {
				System.out.println("全勝ボーナスとして更に200万追加されます！すごいですね！！");
				score += 200;
			} else if (win == 4) {
				System.out.println("4勝ボーナスとして更に100万追加されます！おめでとう！");
				score += 100;
			}
			System.out.println();
			win = 0;
			lose = 0;
		}

		//5回戦まで終了
		Thread.sleep(1000);
		System.out.println("大  会  終  了 !");
		System.out.println("結果発表！");
		System.out.printf("%sさんの獲得賞金は", name);
		Thread.sleep(2000);
		System.out.printf("%d万!!%n", score);
		Thread.sleep(2000);
		System.out.println();

		//レコード更新処理
		if (score > scores.point) {
			scores.name = name;
			scores.point = score;
			scores.day = Dateget();
			System.out.println("new Record!");
			FileWrite(scores);
		}

		//ED
		if (score >= 2000) {
			System.out.println("あなたは真の強者！！勝つべくして勝ったお方！");
			System.out.printf("賞金%d万を手に豪遊するのであった fin", score);
		} else if (score > 1000) {
			System.out.println("強い！！次回があればもっと上を狙ってみよう！");
			System.out.printf("賞金%d万を手にウキウキで帰路についた fin", score);
		} else if (score > 0) {
			System.out.println("うーん・・・まぁ普通かな・・・次回あればがんばってみよう！");
			System.out.printf("賞金%d万を手に帰路についた fin", score);
		} else {
			System.out.println("おっと・・・マイナスの方ですね・・・");
			Thread.sleep(2000);
			System.out.println("あなたは黒服に連れられ姿を消した・・・ fin");
		}
	}

	static class HighScore {
		String name;
		int point;
		String day;
		HighScore(String name, int point, String day) {
			this.name = name;
			this.point = point;
			this.day = day;
		}
	}

	public static void FileWrite(HighScore scores) throws Exception {
		FileOutputStream fos = new FileOutputStream("point.csv");
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		BufferedWriter bw = new BufferedWriter(osw);
		bw.append(scores.name + "," + (Integer.toString(scores.point)) + "," + scores.day);
		bw.close();
	}

	public static String Dateget() {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String date = sdf.format(today);
		return date;
	}

	public static void Show(int round, int score, Enemy[] enemys) throws Exception {
		System.out.println("◆◇◆　対　戦　相　手　　◆◇◆");
		if (round == 0) {
			for (int i = 1 + round; i <= 4 - round; i++) {
				System.out.printf("◆◇◆ %d回戦  %s◆◇◆%n", i, enemys[i].Ename);
				if (round == 0) {Thread.sleep(2000);}
			}
			System.out.println("◆◇◆ 5回戦  ???         ◆◇◆");
		} else {
			System.out.printf("◆◇◆ %d回戦  %s◆◇◆%n", round+1, enemys[round+1].Ename);
		}
		System.out.printf("獲得賞金:%d万%n", score);
		System.out.println();
	}

	public static void Dialogue(int pattern) {
		switch (pattern) {
		case 1:
			System.out.println("「君ここは初めてだろ？私で練習するといい。」");
			break;
		case 3:
			System.out.println("「固い岩のようなカードをくらえ！」");
			break;
		case 4:
			System.out.println("「全てを・・・切る！」");
			break;
		case 5:
			System.out.println("「包み込む男・・・」");
			break;
		case 6:
			System.out.println("「グーチョキパーの順番で出すね、本当だよ。」");
			break;
		case 0:
			System.out.println("「エッ！じゃんけんすればいい・・・ってコト！？」");
			break;
		default:
			System.out.println("「・・・。」");
			break;
		}
	}

	public static void Info(int pattern) {
		//「手カード」情報
		System.out.print("「手」カード ：");
		switch (pattern) {
		case 1:
		case 6:
			System.out.println("バランスよく持っているようです。");
			break;
		case 3:
			System.out.println("グーに偏っているようです。");
			break;
		case 4:
			System.out.println("チョキに偏っているようです。");
			break;
		case 5:
			System.out.println("パーに偏っているようです。");
			break;
		case 0:
		case 2:
			System.out.println("5枚以上持っているように見えます・・・。");
			break;
		}
		//「傾向」情報
		System.out.print("出す手の傾向:");
		switch (pattern) {
		case 1:
			System.out.println("「グー」「チョキ」「パー」の順番で出す。");
			break;
		case 2:
			System.out.println("こちらのカードを把握 枚数の「多い」ものを");
			System.out.println("出すと予想しているようだ。");
			break;
		case 3:
			System.out.println("「グー」に絶対の自信を持つようだ。");
			break;
		case 4:
			System.out.println("「チョキ」で勝ちたいようだ。");
			break;
		case 5:
			System.out.println(" 「パー」！とにかく「パー」！");
			break;
		case 6:
			System.out.println(" 言っていることは全て嘘のようだ。");
			break;
		case 0:
			System.out.println(" 正直何を考えているのかわからない・・・。");
			break;
		}
	}

	public static int Jyan(int pattern, int raund, int gu, int cyoki, int pa) {
		if (pattern == 1) {                            //vsトクイ
			if (raund == 0 || raund == 3) {
				return 1;
			} else if (raund == 1 || raund == 4) {
				return 2;
			} else if (raund == 2) {
				return 3;
			}
		} else if (pattern == 3) {
			return 1;
		} else if (pattern == 4) {
			return 2;
		} else if (pattern == 5) {
			return 3;
		} else if (pattern == 6) {
			if (raund == 0 || raund == 3) {
				return 3;
			} else if (raund == 1 || raund == 4) {
				return 1;
			} else if (raund == 2) {
				return 2;
			}
		} else if (pattern == 0) {
			int a = new Random().nextInt(3) + 1;
			return a;
		} else if (pattern == 2) {                     //vsイトウ
			if (gu > cyoki && gu > pa) {
				return 3;
			} else if (cyoki > gu && cyoki > pa) {
				return 1;
			} else if (pa > gu && pa > cyoki) {
				return 2;
			} else if (gu > cyoki && gu == pa) {
				return 3;
			} else if (cyoki > gu && cyoki == pa) {
				return 2;
			} else if (gu > pa && gu == cyoki) {
				return 1;
			} else {
				return 1;
			}
		}
		return 1;
	}
}

//エネミークラス  ※エネミー側の「カード」管理は一旦無し
class Enemy {
	String Ename;
	//int Egu;
	//int Ecyoki;
	//int Epa;
	int pattern;
	//Enemy(String Ename,int Egu,int Ecyoki,int Epa,int pattern){
	Enemy(String Ename, int pattern) {
		this.Ename = Ename;
		//this.Egu=Egu;
		//this.Ecyoki=Ecyoki;
		//this.Epa=Epa;
		this.pattern = pattern;
	}
}