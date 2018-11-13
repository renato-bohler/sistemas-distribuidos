#include "buttondelegate.h"

#include <QtDebug>
#include <QApplication>
#include <QMouseEvent>
#include <QMessageBox>
#include <QPainter>
#include <QStyleOption>
#include <QDesktopWidget>

ButtonDelegate::ButtonDelegate(QObject *parent) :
    QItemDelegate(parent) {
}


void ButtonDelegate::paint(QPainter *painter, const QStyleOptionViewItem &option, const QModelIndex &index) const {
    QPair<QStyleOptionButton*, QStyleOptionButton*>* buttons = m_btns.value(index);
    if (!buttons) {
        QStyleOptionButton* button1 = new QStyleOptionButton();
        button1->text = "Comprar";

        QStyleOptionButton* button2 = new QStyleOptionButton();
        button2->text = 'Y';
        button2->state |= QStyle::State_Enabled;
        buttons =new  QPair<QStyleOptionButton*, QStyleOptionButton*>(button1, button2);
        (const_cast<ButtonDelegate *>(this))->m_btns.insert(index, buttons);
    }
    buttons->first->rect = option.rect.adjusted(0, 0, -(option.rect.width() / 4 + 0) , -0);
    buttons->second->rect = option.rect.adjusted(buttons->first->rect.width() + 4, 4, -4, -4);
    painter->save();

    if (option.state & QStyle::State_Selected) {
        painter->fillRect(option.rect, option.palette.highlight());

    }
    painter->restore();
    QApplication::style()->drawControl(QStyle::CE_PushButton, buttons->first, painter);
}

bool ButtonDelegate::editorEvent(QEvent *event, QAbstractItemModel *model, const QStyleOptionViewItem &option, const QModelIndex &index) {
    if (event->type() == QEvent::MouseButtonRelease) {
        QMouseEvent* e =(QMouseEvent*)event;

        if (m_btns.contains(index)) {
            QPair<QStyleOptionButton*, QStyleOptionButton*>* btns = m_btns.value(index);
            if (btns->first->rect.contains(e->x(), e->y())) {
                btns->first->state &= (~QStyle::State_Sunken);
                // showMsg(tr('btn1 column %1').arg(index.column()));
                qDebug() << "column" << index.column();
                qDebug() << "data" << index.data();
                qDebug() << "parent" << index.parent();
                qDebug() << "row" << index.row();
            } else if(btns->second->rect.contains(e->x(), e->y())) {
                btns->second->state &= (~QStyle::State_Sunken);
                // showMsg(tr('btn2 row %1').arg(index.row()));
                qDebug() << "BTN1" << index.column();
            }
        }
    }
}

void ButtonDelegate::showMsg(QString str) {
    QMessageBox msg;
    msg.setText(str);
    msg.exec();
}
