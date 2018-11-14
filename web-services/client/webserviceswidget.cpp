#include "webserviceswidget.h"
#include "buttondelegate.h"

#include <QtWidgets>
#include <QUrl>
#include <QNetworkAccessManager>
#include <QNetworkRequest>
#include <QNetworkReply>
#include <QJsonValue>
#include <QJsonValueRef>
#include <QJsonDocument>
#include <QJsonObject>
#include <QJsonArray>

/* Classe WebServicesWidget contém toda a lógica das abas de
 * passagens, hospedagens e pacotes.
 */
WebServicesWidget::WebServicesWidget(QWidget *parent)
    : QTabWidget(parent) {
    setupAirfares();
    setupAccommodations();
    setupPackages();
}

// Método slot que realiza chamada para compra de passagens
void WebServicesWidget::buyAirfare(QModelIndex index) {
    QJsonObject airfare = this->airfareTableModelData->getJsonObject(index);

    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/airfare/comprar");

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    manager->post(request, QJsonDocument(airfare).toJson());

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(searchAirfares()));
}

// Método slot que executa a busca por passagens
void WebServicesWidget::searchAirfares() {
    this->loadAirfares(air_origem->text(), air_destino->text(), air_dataIda->text(), air_dataVolta->text(), air_numeroPessoas->text());
}

// Método que realiza chamada para consulta de passagens
void WebServicesWidget::loadAirfares(QString origem, QString destino, QString dataIda, QString dataVolta, QString numeroPessoas) {
    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/airfare");

    QUrlQuery query;
    query.addQueryItem("origem", origem);
    query.addQueryItem("destino", destino);
    query.addQueryItem("dataIda", dataIda);
    query.addQueryItem("dataVolta", dataVolta);
    query.addQueryItem("numeroPessoas", numeroPessoas);
    serviceUrl.setQuery(query);

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(airfaresRequestFinished(QNetworkReply*)));

    manager->get(request);
}

// Método slot callback chamado ao finalizar a chamada de consulta de passagens
void WebServicesWidget::airfaresRequestFinished(QNetworkReply* reply){
    QByteArray buffer = reply->readAll();
    QJsonDocument jsonDocument(QJsonDocument::fromJson(buffer));
    airfareTableModelData->setJson(jsonDocument);
}

// Método slot que realiza chamada para compra de hospedagens
void WebServicesWidget::buyAccommodation(QModelIndex index) {
    QJsonObject accommodation = this->accommodationTableModelData->getJsonObject(index);

    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/accommodation/comprar");

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    manager->post(request, QJsonDocument(accommodation).toJson());

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(searchAccommodations()));
}

// Método slot que executa a busca por hospedagens
void WebServicesWidget::searchAccommodations() {
    this->loadAccommodations(acc_cidade->text(), acc_dataEntrada->text(), acc_dataSaida->text(), acc_numeroQuartos->text(), acc_numeroPessoas->text());
}

// Método que realiza chamada para consulta de hospedagens
void WebServicesWidget::loadAccommodations(QString cidade, QString dataEntrada, QString dataSaida, QString numeroQuartos, QString numeroPessoas) {
    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/accommodation");

    QUrlQuery query;
    query.addQueryItem("cidade", cidade);
    query.addQueryItem("dataEntrada", dataEntrada);
    query.addQueryItem("dataSaida", dataSaida);
    query.addQueryItem("numeroQuartos", numeroQuartos);
    query.addQueryItem("numeroPessoas", numeroPessoas);
    serviceUrl.setQuery(query);

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(accommodationsRequestFinished(QNetworkReply*)));

    manager->get(request);
}

// Método slot callback chamado ao finalizar a chamada de consulta de hospedagens
void WebServicesWidget::accommodationsRequestFinished(QNetworkReply* reply){
    QByteArray buffer = reply->readAll();
    QJsonDocument jsonDocument(QJsonDocument::fromJson(buffer));
    accommodationTableModelData->setJson(jsonDocument);
}

// Método slot que realiza chamada para compra de pacotes
void WebServicesWidget::buyPackage(QModelIndex index) {
    QJsonObject package = this->packageTableModelData->getJsonObject(index);

    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/package/comprar");

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    manager->post(request, QJsonDocument(package).toJson());

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(searchPackages()));
}

// Método slot que executa a busca por pacotes
void WebServicesWidget::searchPackages() {
    this->loadPackages(pkg_origem->text(), pkg_destino->text(), pkg_dataIda->text(), pkg_dataVolta->text(), pkg_numeroQuartos->text(), pkg_numeroPessoas->text());
}

// Método que realiza chamada para consulta de pacotes
void WebServicesWidget::loadPackages(QString origem, QString destino, QString dataIda, QString dataVolta, QString numeroQuartos, QString numeroPessoas) {
    // Inicializa a URL do webservice
    QUrl serviceUrl = QUrl("http://localhost:8080/web-services/package");

    QUrlQuery query;
    query.addQueryItem("origem", origem);
    query.addQueryItem("destino", destino);
    query.addQueryItem("dataIda", dataIda);
    query.addQueryItem("dataVolta", dataVolta);
    query.addQueryItem("numeroQuartos", numeroQuartos);
    query.addQueryItem("numeroPessoas", numeroPessoas);
    serviceUrl.setQuery(query);

    // Chama o webservice
    QNetworkAccessManager *manager = new QNetworkAccessManager(this);

    QNetworkRequest request(serviceUrl);
    request.setHeader(QNetworkRequest::ContentTypeHeader, QVariant(
    QString("application/json")));

    connect(manager, SIGNAL(finished(QNetworkReply*)), this,
        SLOT(packagesRequestFinished(QNetworkReply*)));

    manager->get(request);
}

// Método slot callback chamado ao finalizar a chamada de consulta de pacotes
void WebServicesWidget::packagesRequestFinished(QNetworkReply* reply){
    QByteArray buffer = reply->readAll();
    QJsonDocument jsonDocument(QJsonDocument::fromJson(buffer));
    packageTableModelData->setJson(jsonDocument);
}

// Método que inicializa a aba de passagens
void WebServicesWidget::setupAirfares() {
    QJsonTableModel::Header header;
    header.push_back( QJsonTableModel::Heading({ {"title","Origem"},  {"lv1","ida"},   {"index","origem"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Destino"},  {"lv1","ida"},   {"index","destino"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data ida"},  {"lv1","ida"},   {"index","data"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data volta"},  {"lv1","volta"},   {"index","data"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Nº pessoas"},  {"index","numeroPessoas"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Valor total"},  {"index","valorTotal"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Ação"},          {"index",""} }) );

    airfareTableView = new QTableView;
    airfareTableModelData = new QJsonTableModel( header, this );
    airfareTableView->setModel(airfareTableModelData);
    ButtonDelegate *m_buttonDelegate = new ButtonDelegate(this);
    connect(m_buttonDelegate, SIGNAL (pressed(QModelIndex)), this, SLOT (buyAirfare(QModelIndex)));
    airfareTableView->setItemDelegateForColumn(6, m_buttonDelegate);

    QByteArray json = "[]";
    QJsonDocument jsonDocument = QJsonDocument::fromJson(json);
    airfareTableModelData->setJson(jsonDocument);

    air_origem = new QLineEdit();
    air_origem->setPlaceholderText("Origem");

    air_destino = new QLineEdit();
    air_destino->setPlaceholderText("Destino");

    air_dataIda = new QLineEdit();
    air_dataIda->setPlaceholderText("Data ida");

    air_dataVolta = new QLineEdit();
    air_dataVolta->setPlaceholderText("Data volta");

    air_numeroPessoas = new QLineEdit();
    air_numeroPessoas->setPlaceholderText("Nº pessoas");

    QPushButton *pesquisar = new QPushButton("Pesquisar");
    connect(pesquisar, SIGNAL (pressed()), this, SLOT (searchAirfares()));

    QWidget *widget = new QWidget;
    QGridLayout *grid = new QGridLayout;
    grid->addWidget(air_origem, 0, 0);
    grid->addWidget(air_destino, 0, 1);
    grid->addWidget(air_dataIda, 0, 2);
    grid->addWidget(air_dataVolta, 0, 3);
    grid->addWidget(air_numeroPessoas, 0, 4);
    grid->addWidget(pesquisar, 0, 5);
    grid->addWidget(airfareTableView, 1, 0, 1, 6);

    widget->setLayout(grid);
    addTab(widget, "Passagens");
}

// Método que inicializa a aba de hospedagens
void WebServicesWidget::setupAccommodations() {
    QJsonTableModel::Header header;
    header.push_back( QJsonTableModel::Heading({ {"title","Cidade"},        {"index","cidade"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data entrada"},  {"index","dataEntrada"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data saída"},    {"index","dataSaida"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Nº quartos"},    {"index","numeroQuartos"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Nº pessoas"},    {"index","numeroPessoas"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Valor total"},   {"index","valorTotal"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Ação"},          {"index",""} }) );

    accommodationTableView = new QTableView;
    accommodationTableModelData = new QJsonTableModel( header, this );
    accommodationTableView->setModel(accommodationTableModelData);
    ButtonDelegate *m_buttonDelegate = new ButtonDelegate(this);
    connect(m_buttonDelegate, SIGNAL (pressed(QModelIndex)), this, SLOT (buyAccommodation(QModelIndex)));
    accommodationTableView->setItemDelegateForColumn(6, m_buttonDelegate);

    QByteArray json = "[]";
    QJsonDocument jsonDocument = QJsonDocument::fromJson(json);
    accommodationTableModelData->setJson(jsonDocument);

    acc_cidade = new QLineEdit();
    acc_cidade->setPlaceholderText("Cidade");

    acc_dataEntrada = new QLineEdit();
    acc_dataEntrada->setPlaceholderText("Data entrada");

    acc_dataSaida = new QLineEdit();
    acc_dataSaida->setPlaceholderText("Data saida");

    acc_numeroQuartos = new QLineEdit();
    acc_numeroQuartos->setPlaceholderText("Nº quartos");

    acc_numeroPessoas = new QLineEdit();
    acc_numeroPessoas->setPlaceholderText("Nº pessoas");

    QPushButton *pesquisar = new QPushButton("Pesquisar");
    connect(pesquisar, SIGNAL (pressed()), this, SLOT (searchAccommodations()));

    QWidget *widget = new QWidget;
    QGridLayout *grid = new QGridLayout;
    grid->addWidget(acc_cidade, 0, 0);
    grid->addWidget(acc_dataEntrada, 0, 1);
    grid->addWidget(acc_dataSaida, 0, 2);
    grid->addWidget(acc_numeroQuartos, 0, 3);
    grid->addWidget(acc_numeroPessoas, 0, 4);
    grid->addWidget(pesquisar, 0, 5);
    grid->addWidget(accommodationTableView, 1, 0, 1, 6);

    widget->setLayout(grid);

    addTab(widget, "Hospedagens");
}

// Método que inicializa a aba de pacotes
void WebServicesWidget::setupPackages() {
    QJsonTableModel::Header header;
    header.push_back( QJsonTableModel::Heading({ {"title","Origem"},        {"lv1","passagem"},     {"lv2", "ida"},    {"index","origem"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Destino"},       {"lv1","passagem"},     {"lv2", "ida"},    {"index","destino"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data ida"},      {"lv1","passagem"},     {"lv2", "ida"},    {"index","data"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Data volta"},    {"lv1","passagem"},     {"lv2", "volta"},  {"index","data"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Nº quartos"},    {"lv1","hospedagem"},   {"index","numeroQuartos"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Nº pessoas"},    {"lv1","hospedagem"},   {"index","numeroPessoas"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Preço/quarto"},  {"lv1","hospedagem"},   {"index","precoPorQuarto"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Preço/pessoa"},  {"lv1","hospedagem"},   {"index","precoPorPessoa"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Valor total"},   {"index","valorTotal"} }) );
    header.push_back( QJsonTableModel::Heading({ {"title","Ação"},          {"index",""} }) );

    packageTableView = new QTableView;
    packageTableModelData = new QJsonTableModel( header, this );
    packageTableView->setModel(packageTableModelData);
    ButtonDelegate *m_buttonDelegate = new ButtonDelegate(this);
    connect(m_buttonDelegate, SIGNAL (pressed(QModelIndex)), this, SLOT (buyPackage(QModelIndex)));
    packageTableView->setItemDelegateForColumn(9, m_buttonDelegate);

    QByteArray json = "[]";
    QJsonDocument jsonDocument = QJsonDocument::fromJson(json);
    packageTableModelData->setJson(jsonDocument);

    pkg_origem = new QLineEdit();
    pkg_origem->setPlaceholderText("Origem");

    pkg_destino = new QLineEdit();
    pkg_destino->setPlaceholderText("Destino");

    pkg_dataIda = new QLineEdit();
    pkg_dataIda->setPlaceholderText("Data ida");

    pkg_dataVolta = new QLineEdit();
    pkg_dataVolta->setPlaceholderText("Data volta");

    pkg_numeroQuartos = new QLineEdit();
    pkg_numeroQuartos->setPlaceholderText("Nº quartos");

    pkg_numeroPessoas = new QLineEdit();
    pkg_numeroPessoas->setPlaceholderText("Nº pessoas");

    QPushButton *pesquisar = new QPushButton("Pesquisar");
    connect(pesquisar, SIGNAL (pressed()), this, SLOT (searchPackages()));

    QWidget *widget = new QWidget;
    QGridLayout *grid = new QGridLayout;
    grid->addWidget(pkg_origem, 0, 0);
    grid->addWidget(pkg_destino, 0, 1);
    grid->addWidget(pkg_dataIda, 0, 2);
    grid->addWidget(pkg_dataVolta, 0, 3);
    grid->addWidget(pkg_numeroQuartos, 0, 4);
    grid->addWidget(pkg_numeroPessoas, 0, 5);
    grid->addWidget(pesquisar, 0, 6);
    grid->addWidget(packageTableView, 1, 0, 1, 7);

    widget->setLayout(grid);

    addTab(widget, "Pacotes");
}
