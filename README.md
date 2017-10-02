#Makine öğrenmesi Yöntemleri Kullanarak Twitterde Veri Analizi


###Projenin Amacı

Makine Öğrenmesi Yöntemleri kullanılarak Twitterde teror olaylarını destekleyen kullanıcı ve Hashtaglerin tespit edilmesi.

###Gereksinimler ve Projenin Çalıştırılması.

*[Twitter Apps] (https://apps.twitter.com/) -Twitter üyeliğimiz yoksa twitter e üye olup developer hesabı açmalıyız.Açtığımız developer hesabından bir uygulama oluşturmalıyız.

*Projeyi eclipse ile açmalıyız.

*[Twitter Apps] (https://apps.twitter.com/) - Oluşturduğumuz uygulamandan; 'Consumer Key (API Key)','Consumer Secret (API Secret)','Access Token' ve 'Access Token Secret' keylerini kopyalayarak  TokenTwitterKnnVeriAnalizi.java,
 dosyasında aşagıdaki kod bloğunu bularak gerekli yerlere keyleri yapıştırınız.
		```
			ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true).setOAuthConsumerKey("Consumer Key (API Key)")
						.setOAuthConsumerSecret("Consumer Secret (API Secret)")
						.setOAuthAccessToken("Access Token")
						.setOAuthAccessTokenSecret("Access Token Secret");
		```		


###![Ekran Görüntüsü](screenshoot.png)		