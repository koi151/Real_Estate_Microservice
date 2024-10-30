import React, { ReactNode } from "react";
import styles from './cardItem.module.scss';

interface CardItemProps {
  children: ReactNode;
}

const CardItem: React.FC<CardItemProps> = ({ children }) => {
  return (
    <div className={styles['card-item']}>
      {children}
    </div>
  );
}

export default CardItem;
