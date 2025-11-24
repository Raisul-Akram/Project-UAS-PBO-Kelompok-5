# 🎬 Dokumentasi Proyek: Sistem Pemesanan Tiket Bioskop (CLI)
 
## 👥 Anggota Kelompok 5
Proyek ini disusun untuk memenuhi tugas oleh:

1. **Urfan (2408107010038)**
2. **Keiveen Aldiandra(2408107010085)**
3. **Cut Wynona Andromeda(2408107010097)**
4. **Nayla Khansa Livya(2408107010098)**
5. **Muhammad Razi Siregar(2408107010101)**
6. **Raisul Akram(2408107010107)**


## 📋 Deskripsi Proyek
**Sistem Pemesanan Tiket Bioskop** ini adalah aplikasi berbasis Java *Console* (CLI) yang dirancang untuk mensimulasikan operasional dasar sebuah bioskop. Aplikasi ini menerapkan konsep **Object-Oriented Programming (OOP)** secara menyeluruh untuk mengelola entitas seperti Film, Jadwal, Kursi, dan Pengguna.


### 🛠️ Teknologi & Konsep
* **Bahasa:** Java (JDK 8+)
* **Database:** Serialisasi File (`.ser`)
* **Security:** SHA-256 Hashing untuk password
* **OOP Core:**
    * *Encapsulation* (Private fields, Getter/Setter)
    * *Inheritance* (`Pengguna` mewariskan ke `Admin` dan `Pelanggan`)
    * *Polymorphism* (Overriding method `tampilkanMenu`)
    * *Abstraction* (Abstract class `Pengguna`)

---

## ✨ Fitur Utama

### 1. 🔐 Autentikasi & Keamanan
* **Login Multi-level:** Membedakan akses antara **Admin** dan **Pelanggan**.
* **Password Hashing:** Password disimpan dalam bentuk *hash* SHA-256, bukan *plain text*.
* **Registrasi:** Pengguna baru dapat mendaftar sebagai Pelanggan.

### 2. 👨‍💼 Fitur Admin
* **Manajemen Film:** Tambah, Edit, Hapus, dan Lihat daftar film.
* **Manajemen Jadwal:** Membuat jadwal tayang (menentukan Film, Studio, Waktu, dan Dimensi Kursi).
* **Monitoring:** Melihat semua jadwal aktif dan seluruh riwayat pemesanan tiket di sistem.
* **Manajemen User:** Menambah akun Admin atau Pelanggan baru secara manual.

### 3. 👤 Fitur Pelanggan
* **Pencarian:** Mencari film berdasarkan kata kunci judul.
* **Visualisasi Kursi:** Melihat denah kursi (Matrix baris x kolom) dengan status ketersediaan (O = Kosong, X = Terisi).
* **Pemesanan (Booking):** Memesan tiket dengan memilih kode kursi (contoh: A3, B5).
* **Pembatalan:** Membatalkan tiket yang sudah dipesan (kursi menjadi tersedia kembali).
* **Riwayat:** Melihat daftar tiket yang dimiliki pengguna tersebut.

---

## 🏗️ Struktur Data & Class Diagram (Mermaid)

Berikut adalah gambaran relasi antar *class* dalam sistem ini:

```mermaid
classDiagram
    class SistemBioskop {
        +List~Film~ filmList
        +List~Jadwal~ jadwalList
        +List~Tiket~ tiketList
        +List~Pengguna~ penggunaList
        +simpanData()
        +muatData()
    }

    class Pengguna {
        <<Abstract>>
        #String username
        #String passwordHash
        +tampilkanMenu()*
    }

    class Admin {
        +tambahFilm()
        +tambahJadwal()
    }

    class Pelanggan {
        +pesanTiket()
        +lihatRiwayat()
    }

    class Film {
        -int id
        -String judul
        -String genre
    }

    class Jadwal {
        -LocalDateTime waktu
        -String studio
        -Kursi[][] kursiLayout
    }

    class Tiket {
        -int id
        -LocalDateTime waktuPesan
    }

    class Kursi {
        -String kode
        -boolean tersedia
    }

    SistemBioskop "1" *-- "many" Pengguna
    SistemBioskop "1" *-- "many" Film
    SistemBioskop "1" *-- "many" Jadwal
    Pengguna <|-- Admin
    Pengguna <|-- Pelanggan
    Jadwal "1" --> "1" Film
    Jadwal "1" *-- "many" Kursi
    Tiket "1" --> "1" Jadwal
    Tiket "1" --> "1" Kursi
    Tiket "1" --> "1" Pengguna
```
## CARA MENJALANKAN PROGRAM
* javac -d bin -sourcepath src src/Main.java (Compile)
* java -cp bin main (Running)
### ![WhatsApp Image 2025-11-24 at 16 54 58_2fca044d](https://github.com/user-attachments/assets/9377efdc-b380-4c6d-a6f2-b3386ef484ec)



## PEMBAGIAN TUGAS KELOMPOK
1. **Urfan : File Readme and Source Code**
2. **Keiveen Aldiandra : Full Source Code**
3. **Cut Wynona Andromeda : Membuat PPT and Source Code**
4. **Nayla Khansa Livya: Membuat PPT and Source Code**
5. **Muhammad Razi Siregar:Full Source Code**
6. **Raisul Akram: Full Source Code**
