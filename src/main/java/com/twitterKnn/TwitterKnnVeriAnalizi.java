package com.twitterKnn;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import net.zemberek.erisim.Zemberek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import javax.swing.SwingConstants;

public class TwitterKnnVeriAnalizi extends JFrame {

	private JPanel contentPane;
	private JTextField textAra;

	private static final Zemberek ZEMBEREK = new Zemberek(new TurkiyeTurkcesi());
	static Map<String, Integer> map = new HashMap<String, Integer>();
	static boolean kullanici;
	static boolean hashtag;
	static int[][] sozcukler = new int[100][2];
	static int[][] frekans = new int[100][20];
	private static JTextField textToplamSozcuk;
	private static JTextField textSonuc;
	private JTextPane textTwetler;
	private JTextPane textSozcukNum;
	private JTextPane textSozcukFrekans;
	private JTextField textToplamTweet;

	public static void kokler(String kelime) {
		String path1 = "data.txt";
		try {
			BufferedReader oku = new BufferedReader(new FileReader(path1));
			String satir;
			int sayac = 0;
			satir = oku.readLine();
			while (satir != null) {
				sayac++;
				map.put(satir.trim(), sayac);
				satir = oku.readLine();
			}
			oku.close();

		} catch (FileNotFoundException e) {
			System.out.print("dosya bulunamadi");
		} catch (IOException e) {
			System.out.print("twet.txt IO hatasý");
		}

		String temp = temizle(kelime).trim();
		String[] kelimeler = temp.split(" ");
		textToplamSozcuk.setText(Integer.toString(kelimeler.length));
		for (String sozcuk : kelimeler) {
			String kok = kokBul(sozcuk);
			if (map.containsKey(kok)) {
				sozcukler[map.get(kok)][1] = map.get(kok);
				frekans[map.get(kok)][1] += 1;

			}
		}
		double[] fre_mat = new double[3];
		for (int k = 0; k < 100; k++) {
			sozcukler[k][0] = k;
			frekans[k][0] = k;
		}
		for (int i = 0; i < 100; i++) {
			if (frekans[i][1] != 0 && i < 27) {
				fre_mat[0]++;
			} else if (frekans[i][1] != 0 && i < 64) {
				fre_mat[1]++;
			} else if (frekans[i][1] != 0 && i >= 64) {
				fre_mat[2]++;
			}
		}
		// fre_mat[0]=fre_mat[0]/kelimeler.length;
		// fre_mat[1]=fre_mat[1]/kelimeler.length;
		// fre_mat[2]=fre_mat[2]/kelimeler.length;

		double[][] egitim = { { 0.06827309236947791, 0.0642570281124498, 0.012048192771084338, 1 },
				{ 0.03763440860215054, 0.04121863799283154, 0.016129032258064516, 1 },
				{ 0.015873015873015872, 0.01932367149758454, 0.015873015873015872, 1 },
				{ 0.03506311360448808, 0.03506311360448808, 0.011220196353436185, 1 },
				{ 0.026993865030674847, 0.033128834355828224, 0.01717791411042945, 1 },
				{ 0.06093189964157706, 0.06093189964157706, 0.014336917562724014, 1 },
				{ 0.056372549019607844, 0.05392156862745098, 0.01715686274509804, 1 },
				{ 0.0989010989010989, 0.0989010989010989, 0.03296703296703297, 1 },
				{ 0.023976023976023976, 0.02197802197802198, 0.017982017982017984, 1 },
				{ 0.022099447513812154, 0.026703499079189688, 0.01565377532228361, 1 },
				{ 0.0467706013363029, 0.013363028953229399, 0.0556792873051225, -1 },
				{ 0.024210526315789474, 0.021052631578947368, 0.028421052631578948, -1 },
				{ 0.058091286307053944, 0.012448132780082987, 0.07053941908713693, -1 },
				{ 0.03463855421686747, 0.025602409638554216, 0.0391566265060241, -1 },
				{ 0.03025936599423631, 0.012968299711815562, 0.03602305475504323, -1 },
				{ 0.07537688442211055, 0.01507537688442211, 0.06532663316582915, -1 },
				{ 0.029085872576177285, 0.009695290858725761, 0.029085872576177285, -1 },
				{ 0.025386313465783666, 0.017660044150110375, 0.02759381898454746, -1 },
				{ 0.028286189683860232, 0.004991680532445923, 0.036605657237936774, -1 },
				{ 0.03225806451612903, 0.02932551319648094, 0.04398826979472141, -1 } };

		double[][] uzaklýk = new double[20][2];
		for (int i = 0; i < 20; i++) {
			uzaklýk[i][0] = (Math.sqrt(Math.pow(egitim[i][0] - fre_mat[0], 2) + Math.pow(egitim[i][1] - fre_mat[1], 2)
					+ Math.pow(egitim[i][2] - fre_mat[2], 2)));
			uzaklýk[i][1] = egitim[i][3];
			if (i >= 1) {
				double key = uzaklýk[i][0];
				double key2 = uzaklýk[i][1];
				int j = i - 1;
				while ((j > -1) && (uzaklýk[j][0] > key)) {
					uzaklýk[j + 1][0] = uzaklýk[j][0];
					uzaklýk[j + 1][1] = uzaklýk[j][1];
					j--;
				}
				uzaklýk[j + 1][0] = key;
				uzaklýk[j + 1][1] = key2;
			}
		}
		// System.out.println("sýralý-----------------------------");
		for (int i = 0; i < 3; i++) {
			System.out.println(uzaklýk[i][0] + " " + uzaklýk[i][1]);
		}

		int destekleyen = 0;
		int desteklemeyen = 0;

		for (int t = 0; t < 3; t++) {
			if (uzaklýk[t][1] == -1) {
				destekleyen++;
			} else if (uzaklýk[t][1] == 1) {
				desteklemeyen++;
			}
		}
		if (destekleyen > desteklemeyen) {
			textSonuc.setText("destekleyen");

		} else if (destekleyen < desteklemeyen) {
			textSonuc.setText("desteklemeyen");
		} else {
			textSonuc.setText("");
		}
	}

	public static String kokBul(final String kelime) {
		String s = "kok yok";
		String[] kokler = ZEMBEREK.kokBulucu().stringKokBul(kelime);
		if (kokler == null || kokler.length < 1) {
			return s;
		}
		return ZEMBEREK.dilBilgisi().alfabe().ayikla(kokler[0]);
	}

	public static String temizle(final String sozcuk) {
		return sozcuk.replaceAll("[^a-zA-ZðþçýüöÐÜÇÞÖÝ]", " ").replaceAll("\\s+", " ");
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TwitterKnnVeriAnalizi frame = new TwitterKnnVeriAnalizi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TwitterKnnVeriAnalizi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(248, 248, 255));
		panel.setBounds(0, 0, 894, 795);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblFratniversitesiTwitter = new JLabel("TW\u0130TTER ANAL\u0130Z ARACI");
		lblFratniversitesiTwitter.setForeground(new Color(139, 0, 0));
		lblFratniversitesiTwitter.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 23));
		lblFratniversitesiTwitter.setBounds(349, 17, 398, 55);
		panel.add(lblFratniversitesiTwitter);

		textAra = new JTextField();
		textAra.setToolTipText("kullanici adi veya hashtag");
		textAra.setForeground(new Color(139, 0, 0));
		textAra.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textAra.setBounds(60, 204, 209, 31);
		panel.add(textAra);
		textAra.setColumns(10);

		JLabel lblNewLabel = new JLabel("Hashtag veya Kullan\u0131c\u0131  Seciniz:");
		lblNewLabel.setForeground(new Color(0, 191, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblNewLabel.setBounds(58, 105, 269, 31);
		panel.add(lblNewLabel);

		JLabel lblTwetler = new JLabel("Tweetler");
		lblTwetler.setForeground(new Color(0, 191, 255));
		lblTwetler.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblTwetler.setBounds(71, 280, 166, 29);
		panel.add(lblTwetler);

		JLabel lblSzckNumaras = new JLabel("s\u00F6zc\u00FCk numaras\u0131");
		lblSzckNumaras.setForeground(new Color(0, 191, 255));
		lblSzckNumaras.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblSzckNumaras.setBounds(575, 122, 113, 29);
		panel.add(lblSzckNumaras);

		JLabel lblSzckFrekans = new JLabel("s\u00F6zc\u00FCk frekans\u0131");
		lblSzckFrekans.setForeground(new Color(0, 191, 255));
		lblSzckFrekans.setFont(new Font("Tahoma", Font.ITALIC, 14));
		lblSzckFrekans.setBounds(733, 122, 113, 29);
		panel.add(lblSzckFrekans);

		JLabel lblSnflandrmaSonucu = new JLabel("Analiz Sonucu:");
		lblSnflandrmaSonucu.setForeground(new Color(0, 191, 255));
		lblSnflandrmaSonucu.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblSnflandrmaSonucu.setBounds(448, 604, 160, 29);
		panel.add(lblSnflandrmaSonucu);

		JLabel lblToplamKelimeSays = new JLabel("Toplam kelime:");
		lblToplamKelimeSays.setForeground(new Color(0, 191, 255));
		lblToplamKelimeSays.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblToplamKelimeSays.setBounds(448, 524, 172, 29);
		panel.add(lblToplamKelimeSays);

		textToplamSozcuk = new JTextField();
		textToplamSozcuk.setBounds(618, 526, 179, 29);
		panel.add(textToplamSozcuk);
		textToplamSozcuk.setColumns(10);

		textSonuc = new JTextField();
		textSonuc.setColumns(10);
		textSonuc.setBounds(618, 606, 179, 29);
		panel.add(textSonuc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(71, 314, 348, 311);
		panel.add(scrollPane);

		textTwetler = new JTextPane();
		scrollPane.setViewportView(textTwetler);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(575, 165, 113, 327);
		panel.add(scrollPane_1);

		textSozcukNum = new JTextPane();
		scrollPane_1.setViewportView(textSozcukNum);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(733, 165, 111, 327);
		panel.add(scrollPane_2);

		textSozcukFrekans = new JTextPane();
		scrollPane_2.setViewportView(textSozcukFrekans);

		JLabel lblToplamTweet = new JLabel("Toplam tweet:");
		lblToplamTweet.setForeground(new Color(0, 191, 255));
		lblToplamTweet.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblToplamTweet.setBounds(448, 564, 160, 29);
		panel.add(lblToplamTweet);

		textToplamTweet = new JTextField();
		textToplamTweet.setColumns(10);
		textToplamTweet.setBounds(618, 564, 179, 29);
		panel.add(textToplamTweet);

		CheckboxGroup cg = new CheckboxGroup();
		final Checkbox checkHashtag = new Checkbox("Hashtag", false, cg);
		checkHashtag.setForeground(new Color(255, 255, 255));
		checkHashtag.setBackground(new Color(128, 0, 0));
		checkHashtag.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		checkHashtag.setBounds(174, 151, 95, 31);
		panel.add(checkHashtag);

		final Checkbox checkKullanici = new Checkbox("Kullanýcý", true, cg);
		checkKullanici.setForeground(new Color(255, 255, 255));
		checkKullanici.setBackground(new Color(128, 0, 0));
		checkKullanici.setEnabled(true);
		checkKullanici.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 13));
		checkKullanici.setBounds(60, 151, 95, 31);
		panel.add(checkKullanici);

		JButton btnTweetAra = new JButton("tweet ara");
		btnTweetAra.setForeground(new Color(255, 255, 255));
		btnTweetAra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true).setOAuthConsumerKey("Consumer Key (API Key)")
						.setOAuthConsumerSecret("Consumer Secret (API Secret)")
						.setOAuthAccessToken("Access Token")
						.setOAuthAccessTokenSecret("Access Token Secret");
				String txt_ara = textAra.getText().toString().trim();
				if (!txt_ara.equals("") && txt_ara.length() > 0) {
					TwitterFactory tf = new TwitterFactory(cb.build());
					twitter4j.Twitter twitter = tf.getInstance();
					String giris = textAra.getText();
					Query query = new Query(giris);
					query.setCount(200);
					query.lang("tr");
					QueryResult result;
					ArrayList tweets = null;

					try {
						// textField_6.setText(String.valueOf(kullanici));

						// ArrayList tweets = (ArrayList) result.getTweets();
						if (checkKullanici.getState()) {
							tweets = (ArrayList) twitter.getUserTimeline(giris);
						} else if (checkHashtag.getState()) {
							result = twitter.search(query);
							tweets = (ArrayList) result.getTweets();
						}
						// System.out.println(result.getTweets().size());
						if (tweets.size() > 0) {
							String s = "";
							String s2 = "";
							textToplamTweet.setText(Integer.toString(tweets.size()));

							for (int i = 0; i < tweets.size(); i++) {
								Status t = (Status) tweets.get(i);
								// String user = t.getUser().getScreenName();
								// t.getText();
								s = s + t.getText() + "\n";
							}
							kokler(s);
							textTwetler.setText(s);
							s = "";
							int j = 0;
							s2 = "";
							for (int i = 0; i < sozcukler.length; i++) {
								String b = "    ";
								if (sozcukler[i][0] < 10) {
									b = "      ";
								}
								if (sozcukler[i][0] >= 100) {
									b = "  ";
								}

								s = s + Integer.toString(sozcukler[i][j]) + b + (map.keySet().toArray())[i].toString()
										+ "\n";
								s2 = s2 + Integer.toString(frekans[i][j]) + b + Integer.toString(frekans[i][j + 1])
										+ "\n";
							}
							textSozcukNum.setText(s);
							textSozcukFrekans.setText(s2);
							for (int i = 0; i < sozcukler.length; i++)
								for (int k = 0; k < 2; k++) {
									sozcukler[i][k] = 0;
									frekans[i][k] = 0;
								}
						}
					} catch (TwitterException e1) {

						e1.printStackTrace();
					}
				}
			}
		});

		btnTweetAra.setBackground(new Color(139, 0, 0));
		btnTweetAra.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		btnTweetAra.setBounds(156, 248, 113, 37);
		panel.add(btnTweetAra);

		JLabel label = new JLabel("(Twitter kullanici adi veya hashtag giriniz*)");
		label.setForeground(Color.RED);
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
		label.setBounds(278, 214, 241, 13);
		panel.add(label);

	}
}
