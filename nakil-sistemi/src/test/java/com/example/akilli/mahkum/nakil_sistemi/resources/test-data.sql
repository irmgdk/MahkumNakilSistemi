-- Test verileri
INSERT INTO arac (id, plaka, model, kapasite, tip, aktif, bakimda)
VALUES (100, '06TEST001', 'Test Aracı 1', 10, 'NAKİL', true, false);

INSERT INTO arac (id, plaka, model, kapasite, tip, aktif, bakimda)
VALUES (101, '06TEST002', 'Test Aracı 2', 8, 'ACİL', true, false);

INSERT INTO mahkum (id, ad, soyad, tc_kimlik, risk_seviyesi, aktif)
VALUES (100, 'Test', 'Mahkum1', '11111111111', 'DÜŞÜK', true);

INSERT INTO mahkum (id, ad, soyad, tc_kimlik, risk_seviyesi, aktif)
VALUES (101, 'Test', 'Mahkum2', '22222222222', 'ORTA', true);

INSERT INTO gorevli (id, sicil_no, ad, soyad, tc_kimlik, gorev, rol, aktif, müsait)
VALUES (100, 'TEST001', 'Test', 'Şoför', '33333333333', 'Şoför', 'SOFOR', true, true);

INSERT INTO gorevli (id, sicil_no, ad, soyad, tc_kimlik, gorev, rol, aktif, müsait)
VALUES (101, 'TEST002', 'Test', 'Gardiyan', '44444444444', 'Gardiyan', 'GARDIYAN', true, true);