# Säätutka



**Sovelluksen toteutuksesta:**

Sovellus on toteutettu Androidille natiivina sovelluksena käyttäen Javaa.

Kaupunkeja voi lisätä ja poistaa muuttamalla VAKIOT luokasta KAUPUNGIT muuttujaa.

Sovellus toimii suurella kaupunkimäärällä hyvin, koska graafiset elementit ladataan vasta listaa rullaamalla.

Ulkoisena kirjastona sovellus käyttää Picassoa. Kirjastoa käytetään kuvien näyttämiseen verkko-osoitteen perusteella.




**Eroja tehtävänannosta:**

Pudotusvalikko on kiinnitettynä ruudun yläosaan (tehtävänannossa saattaa rullautua sivun mukana?). Ainakin omasta mielestäni tämä ratkaisu toimii paremmin, koska yhden kaupungin sää mahtuu silti myös pienelle ruudulle. Pidempää listaa selatessa käyttäjän ei tarvitse rullata listan alkuun käyttääkseen pudotusvalikkoa.

Fonttien koot ovat yksikköinä sp, eikä pt. Tällä tavoin sovellus mukailee käyttäjän laitteelle asettamia fonttikoon asetuksia.

Kellonajat ovat muutettu laitteen aikavyöhykkeen mukaisiksi, tehtävänannossa kellonajat oli jätetty UTC-aikaan (ainakin ennusteiden ajat 15:00, 18:00... viittaavat rajapinnan tarjoamiin UTC-aikoihin)

Sateeksi lasketaan lumisateen ja vesisateen yhteenlaskettu määrä, koska ei ole erikseen kenttää lumisateelle, muuten talvisin ei sataisi ollenkaan sovelluksen mukaan. Sademääriin on merkitty kolmen tunnin yhteenlaskettu sademäärä. Kuitenkin jos nykyinen sää ilmoittaa vain viimeisen tunnin sademäärän, ilmoitetaan se käyttäjälle. Käyttöliittymässä lukee kuitenkin 3 h, koska oletettavasti sinä aikana ei ole satanut kuin viimeisen tunnin aikana (muuten olisi 3h sarake vastauksessa).

Tehtävänannon pienissä sääennuste elementtien lämpötiloissa on ristiriita. Kuvassa tekstit näkyvät mustana, mutta nuolella merkitty värikoodi vastaa harmaata väriä. Ohjelmassa lämpötilat ovat merkitty mustalla tekstillä.

Päivämäärä on esitetty numeerisesti selkeyden vuoksi




**Sovelluksen suorittaminen:**

HUOM! Sovellusta suorittaaksesi tarvitset avaimen. Avaimen voit hankkia osoitteesta https://home.openweathermap.org/users/sign_up

Hankittuasi avaimen voit joko

a) Asentaa Githubista löytyvän allekirjoitetun julkaisuversion laitteellesi ja syöttää avaimen joka kerta sovelluksen käynnistyksen yhteydessä. Versio löytyy  tiedostosijainnista /app/release/app-release.apk 

b) Avaimen voit syöttää Java-luokan "VAKIOT" (/app/src/main/java/com.jerry.saatutka/VAKIOT.java) merkkijonomuuttujaan "AVAIN", Jonka jälkeen voit käyttää sovelluksen debug- tai release-versiota. Nopein tapa saada sovellus puhelimelle/emulaattorille on kiinnittää laite tietokoneeseen ja suorittaa projekti Android Studion avulla laitteessasi painamalla "Run" nappia (puhelimen kehittäjäasetuksista tarvitsee sallia tämä). Tällöin laitteellasi asentuu debug-versio. Julkaisuversion APK-tiedoston voi hankkia Android Studion yläpalkista Build --> Generate Signed Bundle / APK. Tällöin tarvitset avaimen, jolla allekirjoitat sovelluksen. Voit myös luoda kyseisestä valikosta uuden avaimen. Julkaisuversion voi hankkia myös ilman allekirjoitusavainta, mutta jotkin laitteet eivät suostu asentamaan kyseisiä paketteja.
