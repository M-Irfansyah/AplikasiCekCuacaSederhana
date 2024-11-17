# AplikasiCekCuacaSederhana
 Tugas 6 - M.Irfansyah(2210010176)
## Deskripsi Program
Aplikasi Cek Cuaca Sederhana adalah aplikasi berbasis Java yang menggunakan API cuaca untuk menampilkan informasi cuaca terkini berdasarkan lokasi yang dimasukkan oleh pengguna. Aplikasi ini menampilkan data seperti suhu, kondisi cuaca, dan informasi tambahan lainnya dengan antarmuka pengguna yang intuitif.
## Komponen GUI

• JFrame: Sebagai kerangka utama aplikasi.

• JPanel: Untuk mengelompokkan komponen GUI.

• JLabel: Menampilkan informasi statis dan hasil cuaca.

• JTextField: Untuk input nama lokasi.

• JButton: Tombol untuk memulai pencarian cuaca.

## Fitur Utama

• Input Lokasi: Pengguna dapat memasukkan nama kota atau lokasi untuk mengecek cuaca.

• Informasi Cuaca Terkini: Menampilkan suhu, kondisi cuaca, kelembapan, dan kecepatan angin.

• Antarmuka Sederhana: Mudah digunakan dengan desain GUI berbasis Swing.

• Integrasi API Cuaca: Mengambil data cuaca secara real-time dari API eksternal.
## Penjelasan Kode
• Kelas utama untuk GUI aplikasi

```
public class CekCuacaSederhanaFrame extends javax.swing.JFrame {
    // Konstanta API untuk mengakses layanan cuaca
    private static final String API_KEY = "88e5827ef1f08d8a49b149a682ae9751";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private DefaultTableModel tableModel; // Model untuk tabel data cuaca

    // Metode untuk mengambil data cuaca dari API
    public String dapatkanDataCuaca(String cityName) {
        String response = "";
        try {
            String urlString = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&units=metric&lang=id";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            response = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
```

• Metode untuk menampilkan data cuaca di GUI
 ```
    public void tampilkanCuaca(String cityName) throws JSONException {
        String jsonData = dapatkanDataCuaca(cityName);
        JSONObject jsonObjek = new JSONObject(jsonData);
        String deskripsiCuaca = jsonObjek.getJSONArray("weather").getJSONObject(0).getString("description");
        String kodeIkon = jsonObjek.getJSONArray("weather").getJSONObject(0).getString("icon");
        double suhu = jsonObjek.getJSONObject("main").getDouble("temp");

        jDiskripsi.setText(deskripsiCuaca);
        jSuhu.setText(suhu + "°C");

        String urlIkon = "http://openweathermap.org/img/wn/" + kodeIkon + "@2x.png";

        try {
            ImageIcon ikonCuaca = new ImageIcon(new URL(urlIkon));
            jIcon.setIcon(ikonCuaca);
        } catch (Exception e) {
            e.printStackTrace();
            jIcon.setText("Gagal memuat ikon cuaca");
        }
    }
```
• Metode untuk menyimpan kota ke file

```
    private void simpanKotaKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("cities.txt"))) {
            for (int i = 0; i < ccKota.getItemCount(); i++) {
                writer.write(ccKota.getItemAt(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```
• Metode untuk memuat kota dari file

```
    private void muatKotaDariFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cities.txt"))) {
            String city;
            while ((city = reader.readLine()) != null) {
                ccKota.addItem(city);
            }
        } catch (IOException e) {
            System.out.println("Tidak ada kota yang tersimpan.");
        }
    }
```
• Metode untuk menyimpan data cuaca ke file

```
    private void simpanDataCuacaKeFile(String cityName, String weatherDescription, double temperature) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("weather_data.csv", true))) {
            writer.write(cityName + "," + weatherDescription + "," + temperature + "°C");
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Data cuaca berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data cuaca.");
            e.printStackTrace();
        }
    }
```
• Metode untuk menghapus kota dari ComboBox

```
    private void hapusKota() {
        String selectedCity = (String) ccKota.getSelectedItem();
        if (selectedCity != null) {
            ccKota.removeItem(selectedCity);
            simpanKotaKeFile();
            JOptionPane.showMessageDialog(this, "Kota " + selectedCity + " berhasil dihapus dari daftar favorit.");
        } else {
            JOptionPane.showMessageDialog(this, "Tidak ada kota yang dipilih untuk dihapus.");
        }
    }
```

• Konstruktor untuk inisialisasi GUI
    
```
    public CekCuacaSederhanaFrame() {
        initComponents();
        muatKotaDariFile();

        tableModel = new DefaultTableModel(new String[]{"Nama Kota", "Cuaca", "Suhu"}, 0);
        tblData.setModel(tableModel);
    }
}
```

• Action listener untuk tombol "Cek Cuaca"

```
private void btnCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {
    String cityName = jKota.getText(); // Ambil nama kota dari JTextField
    if (!cityName.isEmpty()) {
        try {
            tampilkanCuaca(cityName); // Panggil metode untuk menampilkan data cuaca
            
            // Cek apakah kota sudah ada di ComboBox
            boolean exists = false;
            for (int i = 0; i < ccKota.getItemCount(); i++) {
                if (ccKota.getItemAt(i).equalsIgnoreCase(cityName)) {
                    exists = true;
                    break;
                }
            }

            // Tambahkan kota ke ComboBox dan simpan jika belum ada
            if (!exists) {
                ccKota.addItem(cityName);
                simpanKotaKeFile(); // Simpan kota ke file
            }
        } catch (JSONException ex) {
            Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Silakan masukkan nama kota!");
    }
}
```

• Event listener untuk perubahan item di ComboBox kota


```
private void ccKotaItemStateChanged(java.awt.event.ItemEvent evt) {
    if (evt.getStateChange() == ItemEvent.SELECTED) {
        String selectedCity = (String) ccKota.getSelectedItem(); // Dapatkan kota yang dipilih
        jKota.setText(selectedCity); // Set nama kota ke JTextField
    }
}
```

• Action listener untuk tombol "Muat Data"

```
private void btnMuatDataActionPerformed(java.awt.event.ActionEvent evt) {
    tableModel.setRowCount(0); // Bersihkan tabel sebelum memuat data baru
    try (BufferedReader reader = new BufferedReader(new FileReader("weather_data.csv"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(","); // Pisahkan data berdasarkan koma
            if (data.length == 3) {
                tableModel.addRow(new Object[]{data[0], data[1], data[2]}); // Tambahkan data ke tabel
            }
        }
        JOptionPane.showMessageDialog(this, "Data cuaca berhasil dimuat ke tabel.");
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data cuaca.");
        e.printStackTrace();
    }
}
```

• Action listener untuk tombol "Simpan"

```
private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {
    String cityName = jKota.getText(); // Ambil nama kota
    String weatherDescription = jDiskripsi.getText(); // Ambil deskripsi cuaca
    String temperature = jSuhu.getText(); // Ambil suhu

    if (!cityName.isEmpty() && !weatherDescription.isEmpty() && !temperature.isEmpty()) {
        simpanDataCuacaKeFile(cityName, weatherDescription, Double.parseDouble(temperature.replace("°C", "")));
    } else {
        JOptionPane.showMessageDialog(this, "Data cuaca tidak lengkap. Pastikan semua data sudah terisi.");
    }
}
```

• Action listener untuk tombol "Keluar"

```
private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {
    btnKeluar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0); // Tutup aplikasi
        }
    });
}
```

• Action listener untuk tombol "Hapus"

```
private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
    btnHapus.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            hapusKota(); // Hapus kota dari ComboBox
        }
    });
}
```
## Tampilan Pada Saat Aplikasi Di Jalankan
![cek cuaca sederhana](https://github.com/user-attachments/assets/dc5173f5-290c-42a7-b72a-f901a23a375c)


## Indikator Penilaian:

| No  | Komponen         |  Persentase  |
| :-: | --------------   |   :-----:    |
|  1  | Komponen GUI     |    10    |
|  2  | Logika Program   |    20    |
|  3  |  Events          |    10    |
|  4  | Kesesuaian UI    |    20    |
|  5  | Memenuhi Variasi |    40    |
|     | TOTAL        | 100 |

## Pembuat

Nama  : M.Irfansyah

NPM   : 2210010176
