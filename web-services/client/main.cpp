#include "mainwindow.h"

#include <QApplication>

// Função main inicializa a aplicação Qt
int main(int argc, char *argv[]) {
    QApplication app(argc, argv);
    MainWindow mw;
    mw.setFixedSize(920, 670);
    mw.show();
    return app.exec();
}
