#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "webserviceswidget.h"

#include <QMainWindow>

class MainWindow : public QMainWindow {
    Q_OBJECT

public:
    MainWindow();

private:
    WebServicesWidget *webServicesWidget;
};

#endif // MAINWINDOW_H
