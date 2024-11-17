import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author asus
 */
public class CekCuacaSederhanaFrame extends javax.swing.JFrame {
    private static final String API_KEY = "88e5827ef1f08d8a49b149a682ae9751"; // Hanya kunci API saja
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String CITY_FILE_PATH = "data/cities.txt";
    private static final String WEATHER_FILE_PATH = "data/weather_data.csv";
    private DefaultTableModel tableModel;
    
    // Fungsi untuk mengambil data cuaca dari API
    public String dapatkanDataCuaca(String cityName) {
        String response = "";
        try {
            // Buat URL lengkap dengan memasukkan cityName, API_KEY, dan bahasa (lang=id)
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
    
    public void tampilkanCuaca(String cityName) throws JSONException {
        // Dapatkan data cuaca dalam bentuk JSON
        String jsonData = dapatkanDataCuaca(cityName);
        JSONObject jsonObjek = new JSONObject(jsonData);

        // Ambil deskripsi cuaca dan suhu dari JSON
        String deskripsiCuaca = jsonObjek.getJSONArray("weather").getJSONObject(0).getString("description");
        String kodeIkon = jsonObjek.getJSONArray("weather").getJSONObject(0).getString("icon"); // Point 1: Dapatkan kode ikon cuaca
        double suhu = jsonObjek.getJSONObject("main").getDouble("temp");

        // Set teks deskripsi cuaca dan suhu ke JLabel
        jDiskripsi.setText(deskripsiCuaca);
        jSuhu.setText(suhu + "°C");

        // Buat URL untuk ikon cuaca berdasarkan kode ikon
        String urlIkon = "http://openweathermap.org/img/wn/" + kodeIkon + "@2x.png";

        try {
            // Load ikon dari URL dan set ke JLabel
            ImageIcon ikonCuaca = new ImageIcon(new URL(urlIkon));
            jIcon.setIcon(ikonCuaca);
        } catch (Exception e) {
            e.printStackTrace();
            jIcon.setText("Gagal memuat ikon cuaca");
        }
    }
    
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
    
    private void muatKotaDariFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("cities.txt"))) {
            String city;
            while ((city = reader.readLine()) != null) {
                ccKota.addItem(city);
            }
        } catch (IOException e) {
            // Jika file tidak ditemukan, abaikan saja
            System.out.println("No saved cities found.");
        }
    }
    private void simpanDataCuacaKeFile(String cityName, String weatherDescription, double temperature) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("weather_data.csv", true))) {
            // Menuliskan data dalam format CSV
            writer.write(cityName + "," + weatherDescription + "," + temperature + "°C");
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Data cuaca berhasil disimpan!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data cuaca.");
            e.printStackTrace();
        }
    }
      
      // Metode untuk menghapus kota yang dipilih dari ComboBox dan menyimpannya ke file
    private void hapusKota() {
    String selectedCity = (String) ccKota.getSelectedItem();
    if (selectedCity != null) {
        ccKota.removeItem(selectedCity);
        simpanKotaKeFile(); // Simpan perubahan setelah kota dihapus
        JOptionPane.showMessageDialog(this, "Kota " + selectedCity + " berhasil dihapus dari daftar favorit.");
    } else {
        JOptionPane.showMessageDialog(this, "Tidak ada kota yang dipilih untuk dihapus.");
    }
}    
    /**
     * Creates new form CekCuacaSederhanaFrame
     */
    public CekCuacaSederhanaFrame() {
        initComponents();
        System.out.println("C:\\Kuliah\\Semester 5\\PBO 2\\AplikasiCekCuacaSederhana\\Simpan " + System.getProperty("user.dir"));
        muatKotaDariFile();
        
        // Inisialisasi model tabel
        tableModel = new DefaultTableModel(new String[]{"Nama Kota", "Cuaca", "Suhu"}, 0);
        tblData.setModel(tableModel);     
    }
      
     public void itemStateChanged(java.awt.event.ItemEvent evt) throws JSONException {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String selectedCity = (String) ccKota.getSelectedItem();
            tampilkanCuaca(selectedCity);  // Panggil metode untuk menampilkan cuaca kota yang dipilih
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        btnKeluar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jIcon = new javax.swing.JLabel();
        jDiskripsi = new javax.swing.JLabel();
        jSuhu = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnHapus = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnCekCuaca = new javax.swing.JButton();
        btnMuatData = new javax.swing.JButton();
        jKota = new javax.swing.JTextField();
        ccKota = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        jIcon1 = new javax.swing.JLabel();
        jDiskripsi1 = new javax.swing.JLabel();
        jSuhu1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aplikasi Cek Cuaca Sederhana");

        jPanel1.setBackground(new java.awt.Color(102, 204, 0));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel8.setText("Aplikasi Cek Cuaca Sederhana");

        btnKeluar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Nama Kota/Lokasi");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Daftar Kota");

        jIcon.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jIcon.setText("      ");

        jDiskripsi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jDiskripsi.setText("     ");

        jSuhu.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jSuhu.setText("     ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel7.setText("Tabel Data Cuaca");

        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnSimpan.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSimpan.setText("Simpan Lokasi");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnCekCuaca.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnCekCuaca.setText("Cek Cuaca");
        btnCekCuaca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekCuacaActionPerformed(evt);
            }
        });

        btnMuatData.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnMuatData.setText("Muat Data Cuaca");
        btnMuatData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMuatDataActionPerformed(evt);
            }
        });

        jKota.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        ccKota.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ccKota.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ccKotaItemStateChanged(evt);
            }
        });

        tblData.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Nama Kota", "Cuaca", "Suhu"
            }
        ));
        jScrollPane1.setViewportView(tblData);

        jIcon1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jIcon1.setText("Icon Cuaca");

        jDiskripsi1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jDiskripsi1.setText("Diskripsi Cuaca :");

        jSuhu1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jSuhu1.setText("Suhu              :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jLabel8)
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                                                .addComponent(ccKota, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jIcon1)
                                                .addGap(42, 42, 42)
                                                .addComponent(jIcon)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnMuatData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1))
                                        .addGap(18, 18, 18)
                                        .addComponent(jKota, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnCekCuaca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnKeluar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnHapus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSuhu1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSuhu))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDiskripsi1)
                                .addGap(39, 39, 39)
                                .addComponent(jDiskripsi)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addGap(61, 61, 61)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnCekCuaca)
                                    .addComponent(jLabel2))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(ccKota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(btnMuatData))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSimpan))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jIcon1))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(jIcon)))
                        .addGap(88, 88, 88)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jDiskripsi)
                            .addComponent(jDiskripsi1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSuhu)
                            .addComponent(jSuhu1))
                        .addGap(3, 3, 3)
                        .addComponent(btnHapus)
                        .addGap(18, 18, 18)
                        .addComponent(btnKeluar)
                        .addGap(177, 177, 177))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCekCuacaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekCuacaActionPerformed
    String cityName = jKota.getText();
        if (!cityName.isEmpty()) {
         try {
             tampilkanCuaca(cityName);
             
             boolean exists = false;
             for (int i = 0; i < ccKota.getItemCount(); i++) {
                 if (ccKota.getItemAt(i).equalsIgnoreCase(cityName)) {
                     exists = true;
                     break;
                 }
             }
             
             if (!exists) {
                 ccKota.addItem(cityName);
                 simpanKotaKeFile(); // Simpan kota ke file setelah penambahan
             }
         } catch (JSONException ex) {
             Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(Level.SEVERE, null, ex);
         }
        } else {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama kota!");
        }
    }//GEN-LAST:event_btnCekCuacaActionPerformed

    private void ccKotaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ccKotaItemStateChanged
    if (evt.getStateChange() == ItemEvent.SELECTED) {
            String selectedCity = (String) ccKota.getSelectedItem();
            jKota.setText(selectedCity); // Mengisi txtCityName dengan kota yang dipilih
        }
    }//GEN-LAST:event_ccKotaItemStateChanged

    private void btnMuatDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMuatDataActionPerformed
    tableModel.setRowCount(0); // Hapus data lama di tabel sebelum memuat data baru

        try (BufferedReader reader = new BufferedReader(new FileReader("weather_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    // Menambahkan data ke dalam tabel
                    tableModel.addRow(new Object[]{data[0], data[1], data[2]});
                }
            }
            JOptionPane.showMessageDialog(this, "Data cuaca berhasil dimuat ke tabel.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memuat data cuaca.");
            e.printStackTrace();
        }    
    }//GEN-LAST:event_btnMuatDataActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
       // Action untuk tombol "Simpan ke Favorit"
        String cityName = jKota.getText();
        String weatherDescription = jDiskripsi.getText();
        String temperature = jSuhu.getText();

        if (!cityName.isEmpty() && !weatherDescription.isEmpty() && !temperature.isEmpty()) {
            simpanDataCuacaKeFile(cityName, weatherDescription, Double.parseDouble(temperature.replace("°C", "")));
        } else {
            JOptionPane.showMessageDialog(this, "Data cuaca tidak lengkap. Pastikan semua data sudah terisi.");
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
     // Tambahkan action listener untuk tombol keluar
    btnKeluar.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            System.exit(0); // Menutup aplikasi
        }
    });
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
    // Tambahkan action listener untuk tombol hapus
    btnHapus.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            hapusKota();
        }
    });
    }//GEN-LAST:event_btnHapusActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CekCuacaSederhanaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CekCuacaSederhanaFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCekCuaca;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnMuatData;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> ccKota;
    private javax.swing.JLabel jDiskripsi;
    private javax.swing.JLabel jDiskripsi1;
    private javax.swing.JLabel jIcon;
    private javax.swing.JLabel jIcon1;
    private javax.swing.JTextField jKota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jSuhu;
    private javax.swing.JLabel jSuhu1;
    private javax.swing.JTable tblData;
    // End of variables declaration//GEN-END:variables
}
