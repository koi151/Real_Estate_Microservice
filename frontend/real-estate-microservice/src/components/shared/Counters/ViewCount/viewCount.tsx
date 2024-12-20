import { Tooltip } from "antd";
import React from "react";
import { FaRegEye } from "react-icons/fa";

interface ViewCountProps {
  propertyView: number | null | undefined;
}

const ViewCount: React.FC<ViewCountProps> = ({ propertyView }) => {
  return (
    <Tooltip title={`${propertyView} views`} placement="bottom">
      <div className='d-flex justify-content-center align-items-center'>
        <FaRegEye/>
        <span className="ml-1">{propertyView}</span>
      </div>
    </Tooltip>
  );
}

export default ViewCount;
