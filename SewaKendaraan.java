import java.util.ArrayList;
import java.util.Scanner;

class KendaraanTidakTersediaException extends Exception {
    public KendaraanTidakTersediaException(String message) {
        super(message);
    }
}

abstract class Kendaraan {
    private String kodeKendaraan;
    private String namaKendaraan;
    private double hargaSewaPerHari;
    private boolean isTersedia;

    public Kendaraan(String kode, String nama, double hargaSewa) {
        this.kodeKendaraan = kode;
        this.namaKendaraan = nama;
        this.hargaSewaPerHari = hargaSewa;
        this.isTersedia = true; // Default tersedia
    }

    // Getters and Setters
    public String getKodeKendaraan() { 
        return kodeKendaraan; 
    }
    public void setKodeKendaraan(String kode) { 
        this.kodeKendaraan = kode; 
    }
    public String getNamaKendaraan() { 
        return namaKendaraan; 
    }
    public void setNamaKendaraan(String nama) { 
        this.namaKendaraan = nama; 
    }
    public double getHargaSewaPerHari() { 
        return hargaSewaPerHari; 
    }
    public void setHargaSewaPerHari(double harga) { 
        this.hargaSewaPerHari = harga; 
    }
    public boolean isTersedia() { 
        return isTersedia; 
    }
    public void setTersedia(boolean status) { 
        this.isTersedia = status; 
    }

    // Abstract methods untuk Polymorphism
    public abstract void tampilInfo();
    public abstract double hitungBiayaDasar(int lamaSewa);
}

// 3. INHERITANCE (Mobil)
class Mobil extends Kendaraan {
    private int jumlahKursi;

    public Mobil(String kode, String nama, double harga, int jumlahKursi) {
        super(kode, nama, harga);
        this.jumlahKursi = jumlahKursi;
    }

    public int getJumlahKursi() { 
        return jumlahKursi; 
    }
    public void setJumlahKursi(int jumlahKursi) { 
        this.jumlahKursi = jumlahKursi; 
    }

    @Override
    public void tampilInfo() {
        String status = isTersedia() ? "Tersedia" : "Disewa";
        System.out.printf("[MOBIL] Kode: %-6s | Nama: %-18s | Kursi: %-2d | Tarif: Rp%,.0f/hari | Status: %s\n", 
            getKodeKendaraan(), getNamaKendaraan(), jumlahKursi, getHargaSewaPerHari(), status);
    }

    @Override
    public double hitungBiayaDasar(int lamaSewa) {
        double total = lamaSewa * getHargaSewaPerHari();
        if (jumlahKursi > 5) {
            total += 50000; // Tambahan biaya perawatan
        }
        return total;
    }
}

// 4. INHERITANCE (Motor)
class Motor extends Kendaraan {
    private String jenisTransmisi;

    public Motor(String kode, String nama, double harga, String jenisTransmisi) {
        super(kode, nama, harga);
        this.jenisTransmisi = jenisTransmisi;
    }

    public String getJenisTransmisi() { return jenisTransmisi; }
    public void setJenisTransmisi(String jenis) { this.jenisTransmisi = jenis; }

    @Override
    public void tampilInfo() {
        String status = isTersedia() ? "Tersedia" : "Disewa";
        System.out.printf("[MOTOR] Kode: %-6s | Nama: %-18s | Transmisi: %-5s | Tarif: Rp%,.0f/hari | Status: %s\n", 
            getKodeKendaraan(), getNamaKendaraan(), jenisTransmisi, getHargaSewaPerHari(), status);
    }

    @Override
    public double hitungBiayaDasar(int lamaSewa) {
        double total = lamaSewa * getHargaSewaPerHari();
        if (jenisTransmisi.equalsIgnoreCase("Matik")) {
            total += (10000 * lamaSewa); // Tambahan biaya asuransi per hari
        }
        return total;
    }
}

// 5. SYSTEM CLASS (Mengelola Collection dan Logika)
class GoDriveRentalSystem {
    // Menggunakan ArrayList untuk Collection
    private ArrayList<Kendaraan> daftarKendaraan = new ArrayList<>();

    public void tambahKendaraan(Kendaraan k) {
        daftarKendaraan.add(k);
        System.out.println("[INFO] Kendaraan berhasil ditambahkan: " + k.getNamaKendaraan() + " (" + k.getKodeKendaraan() + ")");
    }

    public void tampilkanDaftarKendaraan() {
        System.out.println("\n=== DAFTAR ARMADA GODRIVE ===");
        if (daftarKendaraan.isEmpty()) {
            System.out.println("Belum ada data kendaraan.");
            return;
        }
        for (int i = 0; i < daftarKendaraan.size(); i++) {
            System.out.print((i + 1) + ". ");
            daftarKendaraan.get(i).tampilInfo(); // Polymorphism beraksi di sini
        }
    }

    public void sewaKendaraan(String kode, int lamaSewa, boolean isVIP) throws KendaraanTidakTersediaException {
        Kendaraan kendaraanDitemukan = null;
        
        for (Kendaraan k : daftarKendaraan) {
            if (k.getKodeKendaraan().equalsIgnoreCase(kode)) {
                kendaraanDitemukan = k;
                break;
            }
        }

        // Lempar exception jika tidak ditemukan atau sedang disewa
        if (kendaraanDitemukan == null || !kendaraanDitemukan.isTersedia()) {
            throw new KendaraanTidakTersediaException("Kendaraan dengan kode " + kode + " gagal disewa. Alasan: Kendaraan sedang disewa atau tidak ditemukan!");
        }

        // Proses Penyewaan
        kendaraanDitemukan.setTersedia(false);
        double totalBiaya = kendaraanDitemukan.hitungBiayaDasar(lamaSewa);
        
        System.out.println("\n=== TRANSAKSI SEWA GODRIVE ===");
        System.out.println("Kendaraan Berhasil Disewa!");
        System.out.println("Unit\t\t: " + kendaraanDitemukan.getNamaKendaraan() + " (" + kendaraanDitemukan.getKodeKendaraan() + ")");
        System.out.println("Lama Sewa\t: " + lamaSewa + " hari");
        
        System.out.printf("Biaya Dasar Harian : Rp %,.0f\n", (kendaraanDitemukan.getHargaSewaPerHari() * lamaSewa));
        
        // Cek tambahan khusus untuk ditampilkan ke struk
        if (kendaraanDitemukan instanceof Mobil && ((Mobil) kendaraanDitemukan).getJumlahKursi() > 5) {
            System.out.println("Tambahan Kursi (>5): Rp 50,000");
        } else if (kendaraanDitemukan instanceof Motor && ((Motor) kendaraanDitemukan).getJenisTransmisi().equalsIgnoreCase("Matik")) {
            System.out.printf("Asuransi Matik\t   : Rp %,.0f\n", (10000.0 * lamaSewa));
        }

        // Diskon Opsional
        double diskon = 0;
        if (isVIP) {
            diskon += (totalBiaya * 0.10);
            System.out.printf("Diskon Member VIP (10%): -Rp %,.0f\n", diskon);
        }
        if (lamaSewa > 7) {
            double diskonWaktu = (totalBiaya * 0.05); // Misal diskon 5% untuk > 7 hari
            diskon += diskonWaktu;
            System.out.printf("Diskon > 7 Hari (5%%): -Rp %,.0f\n", diskonWaktu);
        }
        
        totalBiaya -= diskon;

        System.out.println("------------------------------------");
        System.out.printf("TOTAL BIAYA AKHIR: Rp %,.0f\n", totalBiaya);
    }

    public void kembalikanKendaraan(String kode) {
        for (Kendaraan k : daftarKendaraan) {
            if (k.getKodeKendaraan().equalsIgnoreCase(kode)) {
                if (!k.isTersedia()) {
                    k.setTersedia(true);
                    System.out.println("[INFO] Kendaraan " + k.getNamaKendaraan() + " (" + k.getKodeKendaraan() + ") berhasil dikembalikan. Status: Tersedia.");
                } else {
                    System.out.println("[INFO] Kendaraan tersebut sudah berstatus tersedia (tidak sedang disewa).");
                }
                return;
            }
        }
        System.out.println("[ERROR] Kendaraan dengan kode " + kode + " tidak ditemukan.");
    }
}

// MAIN CLASS (Menu Console)
public class SewaKendaraan {
    public static void main(String[] args) throws KendaraanTidakTersediaException {
        Scanner input = new Scanner(System.in);
        GoDriveRentalSystem sistem = new GoDriveRentalSystem();

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n====== MENU GO DRIVE RENTAL SYSTEM ======");
            System.out.println("1. Tambah Kendaraan");
            System.out.println("2. Tampilkan Daftar Armada");
            System.out.println("3. Sewa Kendaraan");
            System.out.println("4. Kembalikan Kendaraan");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            
            int pilihan = input.nextInt();
            input.nextLine(); // Clear buffer

            switch (pilihan) {
                case 1:
                    System.out.print("Masukkan jenis kendaraan (mobil/motor): ");
                    String jenis = input.nextLine();
                    System.out.print("Masukkan kode kendaraan: ");
                    String kode = input.nextLine();
                    System.out.print("Masukkan nama kendaraan: ");
                    String nama = input.nextLine();
                    System.out.print("Masukkan harga sewa per hari: ");
                    double harga = input.nextDouble();
                    input.nextLine();

                    if (jenis.equalsIgnoreCase("mobil")) {
                        System.out.print("Masukkan kapasitas kursi: ");
                        int kursi = input.nextInt();
                        sistem.tambahKendaraan(new Mobil(kode, nama, harga, kursi));
                    } else if (jenis.equalsIgnoreCase("motor")) {
                        System.out.print("Masukkan jenis transmisi (Matik/Manual): ");
                        String transmisi = input.nextLine();
                        sistem.tambahKendaraan(new Motor(kode, nama, harga, transmisi));
                    } else {
                        System.out.println("Jenis kendaraan tidak valid!");
                    }
                    break;
                case 2:
                    sistem.tampilkanDaftarKendaraan();
                    break;
                case 3:
                    System.out.print("Masukkan kode kendaraan yang ingin disewa: ");
                    String kodeSewa = input.nextLine();
                    System.out.print("Masukkan durasi sewa (dalam hari): ");
                    int lamaSewa = input.nextInt();
                    input.nextLine();
                    System.out.print("Apakah Anda Member VIP? (y/n): ");
                    boolean isVIP = input.nextLine().equalsIgnoreCase("y");
                    
                    // Metode ini akan melempar exception yang membuat program crash persis seperti di screenshot output contoh pesan error
                    sistem.sewaKendaraan(kodeSewa, lamaSewa, isVIP);
                    break;
                case 4:
                    System.out.print("Masukkan kode kendaraan yang ingin dikembalikan: ");
                    String kodeKembali = input.nextLine();
                    sistem.kembalikanKendaraan(kodeKembali);
                    break;
                case 5:
                    System.out.println("Terima kasih telah menggunakan Go Drive Rental System!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
        input.close();
    }
}