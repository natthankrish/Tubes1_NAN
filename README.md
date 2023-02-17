# IF2211_Strategi Algoritma 
## *Tubes1_13521100_1352139_13521162*

## **Table of Contents** 
* [Deskripsi Program](#deskripsi-program)
* [Strategi Greedy](#strategi-greedy)
* [Requirement Program](#program-requirement)
* [Menajalankan Program](#menjalankan-program)
* [Nama, Nim, dan Pemabagian Tugas](#anggota-kelompok-dan-pembagian-tugas)

## **Deskripsi Program**
*Galaxio* adalah permainan *battle royale* yang mempertandingkan satu bot dengan beberapa bot kapal yang lain. Setiap pemain akan memiliki sebuah bot kapal. Tujuan akhir dari permainan ini adalah mempertahankan bot yang dimiliki hingga akhir pertandingan. Agar memenangkan pertandingan, setiap bot harus mengimplementasikan strategi tertentu untuk memenangkan pertandingan. Untuk tugas kali ini, strategi yang digunaakn harus berdasarkan algoritma greedy. Oleh karena itu, repositori ini memiliki algoritma greedy yang dapat digunakan untuk memenangkan pertandingan

## **Strategi Greedy** 
Strategi greedy yang kami terapkan dalam tugas besar kali ini adalah *greedy by distance*, *greedy by food*, dan *greedy by defence*, dengan langkah - langkah seperti berikut : 

1. Simpan semua bot lawan yang berukuran lebih kecil dari pada bot pemain <br>
2. Jika tidak ada bot lawan yang berukuran lebih kecil dari pada bot pemain, pemain akan mencari makanan terdekat (dapat berupa *food* atau *superfood*) <br>
3. Jika ada bot lawan yang berukuran lebih kecil dari pada bot pemain, bot lawan akan diurutkan berdasarkan jarak terdekat dengan bot pemain <br> 
4. Jika jarak antara target dengan bot pemain sangat jauh, pemain akan menyalakan afterburner untuk mengejar target sampai jarak dirasa sudah cukup dekat. <br>
5. Jika jarak antara target dengan bot pemain sudah cukup dekat, bot pemain akan mulai mengeksekusi perintah untuk menembakkan torpedoes. <br>
6. Jika ada object yang berpotensi mengurangi nyawa bot pemain, pemain akan memprioritaskan aksi untuk menghindari object tersebut. <br>
7. Jika posisi bot pemain berada terlalu dekat dengan tepian peta, bot pemain akan mengubah heading menuju tengah - tengah peta.<br>


## **Program Requirement** 
Sebelum menjalankan program, ada beberapa program/aplikasi yang harus didownload terlebih dahulu, yaitu : 

| No. | Nama Progam |
|----| --------------|
| 1. | Galaxio *starter pack* |
| 2. | Java (minimal versi 11) |
| 3. | Nodejs |
| 4. | .Net core 3.1 | 
| 5. | .Net core 5.0 | 

## **Menjalankan Program** 

### **Menggunakan file .bat**
1. Download semua *program requirement* seperti yang dijelaskan di atas <br>

2. Download dan *extract* folder *starter-pack* ke sebuah folder lain yang diinginkan <br>

3. Clone repository ini<br>
    ```sh
    $ cd starter-pack/starter-bots
    ```
    ```sh
    $ git clone https://github.com/natthankrish/Tubes1_NAN.git
    ```

4. Jika struktur folder tidak ada yang berubah, silakan buka file yang bernama **run.bat**. Program seharusnya dapat langsung berjalan. Jika program tidak berjalan, silakan lakukan konfigurasi manual sesuai dengan langkah - langkah di bawah ini. <br>

### **Tanpa file .bat (konfigurasi manual)**

Jika file .bat tidak bekerja, konfigurasi manual dapat dilakukan dengan cara 

1. Download semua *program requirement* seperti yang dijelaskan di atas <br>

2. Download dan *extract* folder *starter-pack* ke sebuah folder lain yang diinginkan <br>

3. Clone repository ini<br>
    ```sh
    $ cd starter-pack/starter-bots
    ```
    ```sh
    $ git clone https://github.com/natthankrish/Tubes1_NAN.git
    ```

4. Di dalam folder *starter-pack*, masuk ke dalam folder *runner-publish* dan masukkan *command* `GameRunner.dll`

    ```sh
    $ cd runner-publish
    $ dotner GameRunner.dll
    ```

5. Di dalam folder *starter-pack*, masuk ke dalam folder *engine-publish* dan masukkan *command* `Engine.dll`
    ```sh
    $ cd runner-publish
    $ dotner Engine.dll
    ```

6. Di dalam folder *starter-pack*, masuk ke dalam folder *logger-publish* dan masukkan *command*  `Logger.dll`
    ```sh
    $ cd logger-publish
    $ dotner Logger.dll
    ```

7. Di dalam folder *starter-pack*, masuk ke dalam folder *reference-bot-publish* dan masukkan *command* `dotnet ReferenceBot.dll`
    ```sh
    $ cd reference-bot-publish
    $ dotner ReferenceBot.dll
    ```

8. Ulangi langkah 7 sebayak berapa bot yang diinginkan

## **Anggota Kelompok dan Pembagian Tugas**

| Nama | NIM | Pembagian Tugas |
| ---- | ---------- | --------------|
| Alexander Jason | 13521100 | Algoritma dan Laporan| 
| Nathania Calista Djunaedi | 13521139 | Algoritma dan Laporan|
| Antonio Natthan Krishna | 13521162 | Algoritma dan Laporan|










