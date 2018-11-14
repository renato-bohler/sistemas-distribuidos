#include "buttondelegate.h"

#include <QtDebug>
#include <QApplication>
#include <QMouseEvent>
#include <QMessageBox>
#include <QPainter>
#include <QStyleOption>
#include <QDesktopWidget>

// Classe ButtonDelegate, utilizada como botão dentro da tabela
ButtonDelegate::ButtonDelegate(QObject *parent) :
    QItemDelegate(parent) {
}

// Método que renderiza o botão
void ButtonDelegate::paint(QPainter *painter, const QStyleOptionViewItem &option, const QModelIndex &index) const {
    QPair<QStyleOptionButton*, QStyleOptionButton*>* buttons = m_btns.value(index);
    if (!buttons) {
        QStyleOptionButton* button1 = new QStyleOptionButton();
        button1->text = "Comprar";
        buttons = new QPair<QStyleOptionButton*, QStyleOptionButton*>(button1, NULL);
        (const_cast<ButtonDelegate *>(this))->m_btns.insert(index, buttons);
    }
    buttons->first->rect = option.rect.adjusted(0, 0, -(option.rect.width() / 4 + 0) , -0);
    painter->save();

    if (option.state & QStyle::State_Selected) {
        painter->fillRect(option.rect, option.palette.highlight());

    }
    painter->restore();
    QApplication::style()->drawControl(QStyle::CE_PushButton, buttons->first, painter);
}

// Método para manipular eventos acionados no botão
bool ButtonDelegate::editorEvent(QEvent *event, QAbstractItemModel *model, const QStyleOptionViewItem &option, const QModelIndex &index) {
    if (event->type() == QEvent::MouseButtonRelease) {
        QMouseEvent *e = (QMouseEvent*) event;

        if (m_btns.contains(index)) {
            QPair<QStyleOptionButton*, QStyleOptionButton*>* btns = m_btns.value(index);
            if (btns->first->rect.contains(e->x(), e->y())) {
                emit pressed(index);
            }
        }
    }
}

