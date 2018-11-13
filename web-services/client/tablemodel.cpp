#include "tablemodel.h"
#include <QJsonObject>
#include <QDateTime>
#include <QPushButton>

QJsonTableModel::QJsonTableModel( const QJsonTableModel::Header& header, QObject * parent )
    : QAbstractTableModel( parent )
    , m_header( header ) {

}

bool QJsonTableModel::setJson(const QJsonDocument &json) {
    return setJson( json.array() );
}

bool QJsonTableModel::setJson( const QJsonArray& array ) {
    beginResetModel();
    m_json = array;
    endResetModel();
    return true;
}

QVariant QJsonTableModel::headerData(int section, Qt::Orientation orientation, int role) const {
    if( role != Qt::DisplayRole ) {
        return QVariant();
    }

    switch( orientation ) {
    case Qt::Horizontal:
        return m_header[section]["title"];
    case Qt::Vertical:
        return QVariant();
    }

}

int QJsonTableModel::rowCount(const QModelIndex &parent ) const {
    return m_json.size();
}

int QJsonTableModel::columnCount(const QModelIndex &parent ) const {
    return m_header.size();
}


QJsonObject QJsonTableModel::getJsonObject( const QModelIndex &index ) const {
    const QJsonValue& value = m_json[index.row() ];
    return value.toObject();
}

QVariant QJsonTableModel::data( const QModelIndex &index, int role ) const {
    switch( role ) {
    case Qt::DisplayRole: {
        QJsonObject obj = getJsonObject( index );
        const QMap<QString, QString> map = m_header[index.column()];

        const QString lv1 = map["lv1"];
        if (obj.contains(lv1)) {
            obj = obj[lv1].toObject();

            const QString lv2 = map["lv2"];
            if (obj.contains(lv2)) {
                obj = obj[lv2].toObject();
            }
        }

        const QString& key = map["index"];
        if( obj.contains( key )) {
            QJsonValue v = obj[ key ];

            if( v.isString() ) {
                return v.toString();
            }
            else if( v.isDouble() ) {
                if (key == "data" || key == "dataEntrada" || key == "dataSaida") {
                    QDateTime timestamp;
                    timestamp.setTime_t(v.toDouble() / 1000);
                    return timestamp.toString("dd/MM/yyyy");
                }
                return QString::number( v.toDouble() );
            }
            else {
                return QVariant();
            }
        }
        else {
            return QVariant();
        }
    }
    case Qt::ToolTipRole:
        return QVariant();
    default:
        return QVariant();
    }
}
