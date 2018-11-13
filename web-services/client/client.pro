QT += widgets
QT += network
requires(qtConfig(listview))

SOURCES   = \
            main.cpp \
            mainwindow.cpp \
            tablemodel.cpp \
    webserviceswidget.cpp \
    buttondelegate.cpp
HEADERS   = \
            mainwindow.h \
            tablemodel.h \
    webserviceswidget.h \
    buttondelegate.h

# install
target.path = /
INSTALLS += target
