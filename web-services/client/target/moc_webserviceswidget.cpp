/****************************************************************************
** Meta object code from reading C++ file 'webserviceswidget.h'
**
** Created by: The Qt Meta Object Compiler version 67 (Qt 5.12.0)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../webserviceswidget.h"
#include <QtCore/qbytearray.h>
#include <QtCore/qmetatype.h>
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'webserviceswidget.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 67
#error "This file was generated using the moc from 5.12.0. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
QT_WARNING_PUSH
QT_WARNING_DISABLE_DEPRECATED
struct qt_meta_stringdata_WebServicesWidget_t {
    QByteArrayData data[13];
    char stringdata0[214];
};
#define QT_MOC_LITERAL(idx, ofs, len) \
    Q_STATIC_BYTE_ARRAY_DATA_HEADER_INITIALIZER_WITH_OFFSET(len, \
    qptrdiff(offsetof(qt_meta_stringdata_WebServicesWidget_t, stringdata0) + ofs \
        - idx * sizeof(QByteArrayData)) \
    )
static const qt_meta_stringdata_WebServicesWidget_t qt_meta_stringdata_WebServicesWidget = {
    {
QT_MOC_LITERAL(0, 0, 17), // "WebServicesWidget"
QT_MOC_LITERAL(1, 18, 14), // "searchAirfares"
QT_MOC_LITERAL(2, 33, 0), // ""
QT_MOC_LITERAL(3, 34, 20), // "searchAccommodations"
QT_MOC_LITERAL(4, 55, 14), // "searchPackages"
QT_MOC_LITERAL(5, 70, 23), // "airfaresRequestFinished"
QT_MOC_LITERAL(6, 94, 14), // "QNetworkReply*"
QT_MOC_LITERAL(7, 109, 29), // "accommodationsRequestFinished"
QT_MOC_LITERAL(8, 139, 23), // "packagesRequestFinished"
QT_MOC_LITERAL(9, 163, 10), // "buyAirfare"
QT_MOC_LITERAL(10, 174, 11), // "QModelIndex"
QT_MOC_LITERAL(11, 186, 16), // "buyAccommodation"
QT_MOC_LITERAL(12, 203, 10) // "buyPackage"

    },
    "WebServicesWidget\0searchAirfares\0\0"
    "searchAccommodations\0searchPackages\0"
    "airfaresRequestFinished\0QNetworkReply*\0"
    "accommodationsRequestFinished\0"
    "packagesRequestFinished\0buyAirfare\0"
    "QModelIndex\0buyAccommodation\0buyPackage"
};
#undef QT_MOC_LITERAL

static const uint qt_meta_data_WebServicesWidget[] = {

 // content:
       8,       // revision
       0,       // classname
       0,    0, // classinfo
       9,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: name, argc, parameters, tag, flags
       1,    0,   59,    2, 0x0a /* Public */,
       3,    0,   60,    2, 0x0a /* Public */,
       4,    0,   61,    2, 0x0a /* Public */,
       5,    1,   62,    2, 0x0a /* Public */,
       7,    1,   65,    2, 0x0a /* Public */,
       8,    1,   68,    2, 0x0a /* Public */,
       9,    1,   71,    2, 0x0a /* Public */,
      11,    1,   74,    2, 0x0a /* Public */,
      12,    1,   77,    2, 0x0a /* Public */,

 // slots: parameters
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void,
    QMetaType::Void, 0x80000000 | 6,    2,
    QMetaType::Void, 0x80000000 | 6,    2,
    QMetaType::Void, 0x80000000 | 6,    2,
    QMetaType::Void, 0x80000000 | 10,    2,
    QMetaType::Void, 0x80000000 | 10,    2,
    QMetaType::Void, 0x80000000 | 10,    2,

       0        // eod
};

void WebServicesWidget::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        WebServicesWidget *_t = static_cast<WebServicesWidget *>(_o);
        Q_UNUSED(_t)
        switch (_id) {
        case 0: _t->searchAirfares(); break;
        case 1: _t->searchAccommodations(); break;
        case 2: _t->searchPackages(); break;
        case 3: _t->airfaresRequestFinished((*reinterpret_cast< QNetworkReply*(*)>(_a[1]))); break;
        case 4: _t->accommodationsRequestFinished((*reinterpret_cast< QNetworkReply*(*)>(_a[1]))); break;
        case 5: _t->packagesRequestFinished((*reinterpret_cast< QNetworkReply*(*)>(_a[1]))); break;
        case 6: _t->buyAirfare((*reinterpret_cast< QModelIndex(*)>(_a[1]))); break;
        case 7: _t->buyAccommodation((*reinterpret_cast< QModelIndex(*)>(_a[1]))); break;
        case 8: _t->buyPackage((*reinterpret_cast< QModelIndex(*)>(_a[1]))); break;
        default: ;
        }
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        switch (_id) {
        default: *reinterpret_cast<int*>(_a[0]) = -1; break;
        case 3:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 0:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QNetworkReply* >(); break;
            }
            break;
        case 4:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 0:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QNetworkReply* >(); break;
            }
            break;
        case 5:
            switch (*reinterpret_cast<int*>(_a[1])) {
            default: *reinterpret_cast<int*>(_a[0]) = -1; break;
            case 0:
                *reinterpret_cast<int*>(_a[0]) = qRegisterMetaType< QNetworkReply* >(); break;
            }
            break;
        }
    }
}

QT_INIT_METAOBJECT const QMetaObject WebServicesWidget::staticMetaObject = { {
    &QTabWidget::staticMetaObject,
    qt_meta_stringdata_WebServicesWidget.data,
    qt_meta_data_WebServicesWidget,
    qt_static_metacall,
    nullptr,
    nullptr
} };


const QMetaObject *WebServicesWidget::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->dynamicMetaObject() : &staticMetaObject;
}

void *WebServicesWidget::qt_metacast(const char *_clname)
{
    if (!_clname) return nullptr;
    if (!strcmp(_clname, qt_meta_stringdata_WebServicesWidget.stringdata0))
        return static_cast<void*>(this);
    return QTabWidget::qt_metacast(_clname);
}

int WebServicesWidget::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QTabWidget::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 9)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 9;
    } else if (_c == QMetaObject::RegisterMethodArgumentMetaType) {
        if (_id < 9)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 9;
    }
    return _id;
}
QT_WARNING_POP
QT_END_MOC_NAMESPACE
