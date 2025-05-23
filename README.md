# Etiket Service Print

Aplikasi Spring Boot untuk layanan pencetakan etiket secara lokal. Dapat dijalankan langsung, dibungkus sebagai file `.jar`, atau dikonversi menjadi `.exe` dan installer Windows.
versi jre agar jalan di win 7 / 10 32/64

OpenJDK8U-jre_x86-32_windows_hotspot_8u452b09.zip
---

## 🚀 Menjalankan Aplikasi Langsung

Pastikan Anda sudah menginstal Maven dan Java JDK minimal versi 8.

```bash
mvn spring-boot:run
```

## 📦 Build JAR

Untuk membangun aplikasi menjadi file .jar, jalankan perintah berikut:

```bash
mvn clean package
```
File etiket-print-1.0.0.jar akan tersedia di folder target/.

## 🧾 Membuat File .exe (opsional)
Langkah-langkah:
Buat folder dist lalu salin ke dalamnya:

etiket-print-1.0.0.jar

Folder jre (JRE portable, nama folder harus jre)

File icon (opsional), contoh printer.ico

Install Launch4j.

Buat file konfigurasi launch4j.xml dan isi seperti berikut:
```bash
<?xml version="1.0" encoding="UTF-8"?>
<launch4jConfig>
  <dontWrapJar>false</dontWrapJar>
  <headerType>gui</headerType>
  <jar>D:\java installer\dist\etiket-print-1.0.0.jar</jar>
  <outfile>D:\java installer\dist\Etiket Service Print.exe</outfile>
  <errTitle></errTitle>
  <cmdLine></cmdLine>
  <chdir>.</chdir>
  <priority>normal</priority>
  <downloadUrl></downloadUrl>
  <supportUrl></supportUrl>
  <stayAlive>false</stayAlive>
  <restartOnCrash>false</restartOnCrash>
  <manifest></manifest>
  <icon>D:\java installer\printer.ico</icon>
  <jre>
    <path>jre</path>
    <requiresJdk>false</requiresJdk>
    <requires64Bit>false</requires64Bit>
    <minVersion>1.8.0</minVersion>
    <maxVersion></maxVersion>
    <initialHeapSize>128</initialHeapSize>
    <maxHeapSize>512</maxHeapSize>
  </jre>
</launch4jConfig>
```
Buka Launch4j, import file XML tersebut, lalu klik Build Wrapper.

## 🧰 Membuat Installer Windows (opsional)
Untuk mendistribusikan aplikasi dalam bentuk installer .exe:

Langkah-langkah:
Buat folder Setup.

Salin ke dalam folder tersebut:

File Etiket Service Print.exe dari Launch4j

Folder jre

Install Inno Setup.

Buat skrip setup.iss dan isi dengan:

```bash
[Setup]
AppName=Etiket Service Print
AppVersion=1.0.0
DefaultDirName={pf}\Mlenong
DefaultGroupName=Mlenong
OutputBaseFilename=Etiket Print Setup
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
ArchitecturesInstallIn64BitMode=x64
SetupIconFile=D:\java.setup\printer.ico

[Files]
; File utama aplikasi
Source: "D:\java.setup\Etiket Service Print.exe"; DestDir: "{app}"; Flags: ignoreversion

; Salin seluruh isi folder JRE
Source: "D:\java.setup\jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs

[Dirs]
; Lindungi folder JRE dari uninstall kalau diperlukan
Name: "{app}\jre"; Flags: uninsneveruninstall

[Icons]
; Shortcut di Start Menu
Name: "{group}\Etiket Service Print"; Filename: "{app}\Etiket Service Print.exe"

; Shortcut di Desktop
Name: "{userdesktop}\Etiket Service Print"; Filename: "{app}\Etiket Service Print.exe"

[Registry]
Root: HKCU; Subkey: "Software\Microsoft\Windows\CurrentVersion\Run"; \
    ValueType: string; ValueName: "Etiket Service Print"; ValueData: """{app}\Etiket Service Print.exe"""; Flags: uninsdeletevalue

[Run]
; Jalankan aplikasi setelah instalasi (jika tidak dalam mode silent)
Filename: "{app}\Etiket Service Print.exe"; Description: "Jalankan Etiket Print Service"; Flags: nowait postinstall skipifsilent
```

Compile skrip tersebut di Inno Setup untuk menghasilkan installer .exe.
