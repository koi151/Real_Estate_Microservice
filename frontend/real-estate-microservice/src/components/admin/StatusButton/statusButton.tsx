import React, { useEffect, useState } from 'react';
import { Button, message } from 'antd';

import propertiesService from '../../../services/admin/properties.service';
import adminAccountsService from '../../../services/admin/admin-accounts.service';
import propertyCategoriesService from '../../../services/admin/property-categories.service';

interface StatusButtonProps {
  typeofChange: 'changePropertyStatus' | 'changePropertyCategoriesStatus' | 'changeAccountStatus';
  itemId: string;
  status: 'active' | 'inactive';
}

const StatusButton: React.FC<StatusButtonProps> = ({ typeofChange, itemId, status }) => {
  const [currentStatus, setCurrentStatus] = useState<'active' | 'inactive'>(status);

  const handleClickStatus = async () => {
    const newStatus = currentStatus === 'active' ? 'inactive' : 'active';

    try {
      let response;

      if (typeofChange === 'changePropertyStatus') {
        response = await propertiesService.changePropertyStatus(itemId, newStatus);
      } else if (typeofChange === 'changePropertyCategoriesStatus') {
        response = await propertyCategoriesService.changeCategoryStatus(itemId, newStatus);
      } else if (typeofChange === 'changeAccountStatus') {
        // response = await adminAccountsService.changeAccountStatus(itemId, newStatus);
      }

      if (response?.status >= 200 && response?.status < 300) {
        setCurrentStatus(newStatus);
        message.success(`Status updated successfully to "${newStatus}"`, 3);
      } else {
        message.error(`Failed to update status. Server responded with code ${response?.status}`, 2);
      }
    } catch (error) {
      console.error('Error occurred:', error);
      message.error('An error occurred while changing status.', 2);
    }
  };

  useEffect(() => {
    setCurrentStatus(status);
  }, [status]);

  return (
    <div className='item-wrapper__upper-content--status'>
      <p className='status-text'>Status: </p>
      <Button
        type='primary'
        className={`${currentStatus.toLowerCase()}-btn small-btn`}
        onClick={handleClickStatus}
        data-id={itemId}
      >
        {currentStatus.charAt(0).toUpperCase() + currentStatus.slice(1)}
      </Button>
    </div>
  );
};

export default StatusButton;
