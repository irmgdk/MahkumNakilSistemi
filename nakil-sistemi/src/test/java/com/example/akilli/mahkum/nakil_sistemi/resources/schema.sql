-- Test şeması (H2 için)
CREATE TABLE IF NOT EXISTS arac (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plaka VARCHAR(20) UNIQUE,
    model VARCHAR(100),
    kapasite INT,
    tip VARCHAR(20),
    aktif BOOLEAN,
    bakimda BOOLEAN,
    serviste BOOLEAN
);

CREATE TABLE IF NOT EXISTS mahkum (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ad VARCHAR(50),
    soyad VARCHAR(50),
    tc_kimlik VARCHAR(11) UNIQUE,
    risk_seviyesi VARCHAR(20),
    aktif BOOLEAN
);

CREATE TABLE IF NOT EXISTS gorevli (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sicil_no VARCHAR(20) UNIQUE,
    ad VARCHAR(50),
    soyad VARCHAR(50),
    tc_kimlik VARCHAR(11) UNIQUE,
    gorev VARCHAR(100),
    rol VARCHAR(20),
    aktif BOOLEAN,
    müsait BOOLEAN
);

CREATE TABLE IF NOT EXISTS nakil_gorevi (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    baslangic_noktasi VARCHAR(200),
    varis_noktasi VARCHAR(200),
    planlanan_baslangic TIMESTAMP,
    planlanan_varis TIMESTAMP,
    durum VARCHAR(20),
    arac_id BIGINT,
    FOREIGN KEY (arac_id) REFERENCES arac(id)
);

CREATE TABLE IF NOT EXISTS nakil_gorevi_mahkumlar (
    nakil_gorevi_id BIGINT,
    mahkumlar_id BIGINT,
    FOREIGN KEY (nakil_gorevi_id) REFERENCES nakil_gorevi(id),
    FOREIGN KEY (mahkumlar_id) REFERENCES mahkum(id)
);

CREATE TABLE IF NOT EXISTS nakil_gorevi_gorevliler (
    nakil_gorevi_id BIGINT,
    gorevliler_id BIGINT,
    FOREIGN KEY (nakil_gorevi_id) REFERENCES nakil_gorevi(id),
    FOREIGN KEY (gorevliler_id) REFERENCES gorevli(id)
);