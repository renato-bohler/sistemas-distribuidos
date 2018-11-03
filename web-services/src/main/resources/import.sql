-- Flights
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (1, 'Curitiba', 'São Paulo', CURRENT_DATE, 100, 10);
INSERT INTO Flight (id, origem, destino, DATA, vagas, precoUnitario) VALUES (2, 'São Paulo', 'Curitiba', DATEADD('DAY',1, CURRENT_DATE), 70, 20);

-- Accommodation
INSERT INTO Accommodation (id, cidade, dataEntrada, dataSaida, numeroQuartos, numeroPessoas, precoPorQuarto, precoPorPessoa) VALUES (1, 'São Paulo', CURRENT_DATE, DATEADD('DAY',1, CURRENT_DATE), 50, 400, 50, 80);