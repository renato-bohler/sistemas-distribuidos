-- Flights
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (1, 'Curitiba', 'São Paulo', CURRENT_DATE, 100, 10);
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (2, 'Curitiba', 'São Paulo', CURRENT_DATE, 200, 5);
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (3, 'Curitiba', 'Londrina', CURRENT_DATE, 50, 25);
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (4, 'São Paulo', 'Curitiba', DATEADD('DAY',1, CURRENT_DATE), 70, 20);
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (5, 'Londrina', 'São Paulo', DATEADD('DAY',1, CURRENT_DATE), 50, 30);

-- Accommodation
INSERT INTO Accommodation (id, cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, precoPorQuarto, precoPorPessoa) VALUES (1, 'Curitiba', CURRENT_DATE, DATEADD('DAY',1, CURRENT_DATE), 50, 400, 50, 80);
INSERT INTO Accommodation (id, cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, precoPorQuarto, precoPorPessoa) VALUES (2, 'São Paulo', CURRENT_DATE, DATEADD('DAY',1, CURRENT_DATE), 150, 700, 40, 50);
INSERT INTO Accommodation (id, cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, precoPorQuarto, precoPorPessoa) VALUES (3, 'São Paulo', CURRENT_DATE, DATEADD('DAY',1, CURRENT_DATE), 80, 500, 100, 100);
INSERT INTO Accommodation (id, cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, precoPorQuarto, precoPorPessoa) VALUES (4, 'Londrina', CURRENT_DATE, DATEADD('DAY',1, CURRENT_DATE), 40, 250, 40, 50);
