#include "mainwindow.h"

#include <QAction>
#include <QFileDialog>
#include <QMenuBar>

// Classe MainWindow inicializa o Widget principal
MainWindow::MainWindow() {
    webServicesWidget = new WebServicesWidget;
    setCentralWidget(webServicesWidget);
    setWindowTitle(tr("WebServices"));
}
