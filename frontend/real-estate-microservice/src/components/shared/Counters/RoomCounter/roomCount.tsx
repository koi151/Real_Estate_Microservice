import React from 'react';
import { Tooltip } from 'antd';
import { IoBedOutline } from 'react-icons/io5';
import { LuBath } from 'react-icons/lu';

interface RoomCountTooltipProps {
  roomType: string; 
  quantity: number | null; 
}

const RoomCountTooltip: React.FC<RoomCountTooltipProps> = ({ roomType, quantity }) => {
  const title = quantity !== null ? `${quantity} ${roomType}` : `No data of ${roomType.toLowerCase()}`;

  const getIcon = (type: string) => {
    switch (type) {
      case 'Bedroom':
        return <IoBedOutline />;
      case 'Bathroom':
        return <LuBath />;
      default:
        return null;
    }
  };

  const icon = getIcon(roomType);

  return (
    <Tooltip title={title} className="d-flex align-items-center mb-2">
      {icon}
      {quantity !== null ? <span className="mb-1 ml-1">{quantity}</span> : <span>...</span>}
    </Tooltip>
  );
};

export default RoomCountTooltip;
