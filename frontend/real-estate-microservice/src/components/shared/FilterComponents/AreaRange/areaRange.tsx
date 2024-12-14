import { Button, Card, Col, InputNumber, Modal, Radio, RadioChangeEvent, Row, Slider, Space, message } from "antd";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../redux/stores";
import { listingTypeFormatted } from "../../../../helpers/standardizeData";
import { setAreaRange } from "../../../../redux/reduxSlices/filtersSlice";
import { FaArrowRightLong } from "react-icons/fa6";
import { areaRangeValue, areaRangeValueDetail } from "../../../../helpers/filterOptions";
import { useNavigate, useLocation } from "react-router-dom";

interface AreaRangeProps {
  label?: string;
  width?: string;
  text?: string;
  textColor?: string;
  modelDisable?: boolean;
}

const AreaRange: React.FC<AreaRangeProps> = ({
  label,
  text,
  textColor = '#000',
  width = '100%',
  modelDisable 
}) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const { listingType } = useSelector((state: RootState) => state.filters);

  const [isModalOpen, setIsModalOpen] = useState(false);

  const [sliderValue, setSliderValue] = useState<[number, number]>([0, 1000]);
  const [areaRangeString, setAreaRangeString] = useState<string | undefined>(undefined);

  useEffect(() => {
    setSliderValue([0, 1000]);
  }, [listingType]);

  useEffect(() => {
    if (areaRangeString) {
      try {
        const parsed = JSON.parse(areaRangeString);
        if (Array.isArray(parsed) && parsed.length === 2) {
          dispatch(setAreaRange(parsed));
        }
      } catch (error) {
        console.error("Failed to parse areaRangeString:", error);
      }
    }
  }, [areaRangeString, dispatch]);

  const updateUrl = () => {
    const params = new URLSearchParams(location.search);

    // Remove existing area range params
    params.delete("areaFrom");
    params.delete("areaTo");
    params.delete("propertyForRent.areaFrom");
    params.delete("propertyForRent.areaTo");
    params.delete("propertyForSale.areaFrom");
    params.delete("propertyForSale.areaTo");
    params.delete("overallAreaFrom");
    params.delete("overallAreaTo");

    if (sliderValue[1] > 0) {
      if (!listingType) {
        if (sliderValue[0] > 0) params.set("overallAreaFrom", sliderValue[0].toString());
        params.set("overallAreaTo", sliderValue[1].toString());
      } else if (listingType === "rent") {
        if (sliderValue[0] > 0) params.set("propertyForRent.areaFrom", sliderValue[0].toString());
        params.set("propertyForRent.areaTo", sliderValue[1].toString());
      } else if (listingType === "sale") {
        if (sliderValue[0] > 0) params.set("propertyForSale.areaFrom", sliderValue[0].toString());
        params.set("propertyForSale.areaTo", sliderValue[1].toString());
      }
    }

    navigate(`?${params.toString()}`, { replace: true });
  };

  const handleModalOk = () => {
    setIsModalOpen(false);
    updateUrl();
  };

  const handleInputChange = (index: number, value: number | undefined) => {
    const newSliderValue: [number, number] = [...sliderValue];
    newSliderValue[index] = value || 0;
    setSliderValue(newSliderValue);
  };

  const handleSliderChange = (newValue: number | number[]) => {
    if (Array.isArray(newValue)) {
      setSliderValue(newValue as [number, number]);
    }
  };

  const handleRadioChange = (e: RadioChangeEvent) => {
    const selectedValue = e.target.value;
    setIsModalOpen(false);
    setAreaRangeString(selectedValue);
    try {
      const parsed = JSON.parse(selectedValue);
      if (Array.isArray(parsed) && parsed.length === 2) {
        setSliderValue(parsed as [number, number]);
        dispatch(setAreaRange(parsed));
        updateUrl();
      }
    } catch (error) {
      console.error("Failed to parse selected radio value:", error);
    }
  };

  return ( 
    <>
      {!modelDisable ? (
        <div className='price-range'>
          {label && (
            <span style={{marginBottom: ".5rem"}}>{label}</span>
          )}
          <Button
            onClick={() => setIsModalOpen(true)}
            style={{ width: `${width}`, color: `${textColor}` }}
          >
            { text }
          </Button>
          <Modal 
            title={`Select area range - ${listingType ? listingTypeFormatted(listingType) : 'for all'}`} 
            open={isModalOpen} 
            onOk={handleModalOk} 
            onCancel={() => setIsModalOpen(false)}
          >
            <hr />
            <div className='price-range__box'>
              <Row gutter={16}>
                <Col span={10} className='d-flex flex-column align-items-center'>
                  <div className='d-flex'>
                    <b>From: </b>
                    <span className='price-range__box--txt'>
                      {`${sliderValue[0]} m²`}
                    </span>
                  </div>
                  <InputNumber
                    value={sliderValue[0]}
                    onChange={(value) => handleInputChange(0, value ?? undefined)}                    
                    min={0}
                  />
                </Col>
                <Col span={4} className="d-flex align-items-center justify-content-center">
                  <FaArrowRightLong style={{fontSize: "2rem", color: "#666"}}/>
                </Col>
                <Col span={10} className='d-flex flex-column align-items-center'>
                  <div className='d-flex'>
                    <b>To: </b>
                    <span className='price-range__box--txt'>
                      {`${sliderValue[1]} m²`}
                    </span>
                  </div>
                  <InputNumber
                    value={sliderValue[1]}
                    onChange={(value) => handleInputChange(1, value ?? undefined)}
                    min={0}
                  />
                </Col>
                <Col span={24} className='d-flex justify-content-center'>
                  <Slider
                    className='custom-slider'
                    range
                    min={0}
                    max={1000}
                    step={1}
                    value={sliderValue}
                    onChange={handleSliderChange}
                  />
                </Col>

                <Col span={24} className="d-flex justify-content-center">
                  <Radio.Group 
                    onChange={handleRadioChange} 
                    value={areaRangeString}
                    className="custom-radio-group"
                  > 
                    <Space direction="vertical">
                      {areaRangeValue.map((item, index) => (
                        <Radio 
                          key={index} value={item.value} 
                          className="label-light custom-radio"
                        >
                          {item.label}
                        </Radio>
                      ))}
                    </Space>
                  </Radio.Group>
                </Col>
              </Row>
            </div>
          </Modal>
        </div> 
      ) : (
        <Card 
          title='Filter by area range' 
          style={{width: "90%", boxShadow: "rgba(99, 99, 99, 0.1) 0px 2px 8px 0px", height: "fit-content"}}
          className="mt-5"
        >
          <div className='price-range__box' style={{marginTop: 0}}>
            <Row gutter={16}>              
              <Col span={24} className="d-flex">
                <Radio.Group 
                  onChange={handleRadioChange} 
                  value={areaRangeString}
                  className="custom-radio-group"
                > 
                  <Space direction="vertical">
                    {areaRangeValueDetail.map((item, index) => (
                      <Radio 
                        key={index} value={item.value} 
                        className="label-light custom-radio mt-2"
                      >
                        {item.label}
                      </Radio>
                    ))}
                  </Space>
                </Radio.Group>
              </Col>
            </Row>
          </div>
        </Card>
      )}
    </>
  )
}

export default AreaRange;
