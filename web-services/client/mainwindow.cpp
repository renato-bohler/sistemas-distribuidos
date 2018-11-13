#include "mainwindow.h"

#include <QAction>
#include <QFileDialog>
#include <QMenuBar>

MainWindow::MainWindow() {
    webServicesWidget = new WebServicesWidget;
    setCentralWidget(webServicesWidget);
    setWindowTitle(tr("WebServices"));
}
