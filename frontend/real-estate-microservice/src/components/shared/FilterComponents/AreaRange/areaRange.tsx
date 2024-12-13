import { Button, Card, Col, InputNumber, Modal, Radio, RadioChangeEvent, Row, Slider, Space } from "antd";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../../../redux/stores";
import { listingTypeFormatted } from "../../../../helpers/standardizeData";
import { setAreaRange } from "../../../../redux/reduxSlices/filtersSlice";
import { FaArrowRightLong } from "react-icons/fa6";
import { areaRangeValue, areaRangeValueDetail } from "../../../../helpers/filterOptions";

interface AreaRangeProps {
  label?: string;
  width?: string;
  text?: string;
  textColor?: string;
  modelDisable?: boolean,
}

const AreaRange: React.FC<AreaRangeProps> = ({
  label,
  text,
  textColor='#000',
  width='100%',
  modelDisable 
}) => {
  const dispatch = useDispatch();

  const { listingType } = useSelector((state: RootState) => state.filters);

  const [ isModalOpen, setIsModalOpen ] = useState(false);

  const [ sliderValue, setSliderValue ] = useState<[number, number]>([0, 500]);
  const [ priceRangeString, setPriceRangeString ] = useState<string | undefined>(undefined)


  useEffect(() => {
    priceRangeString && dispatch(setAreaRange(JSON.parse(priceRangeString)));
  }, [priceRangeString, dispatch])

  const handleModalOk = () => {
    setIsModalOpen(false);
    dispatch(setAreaRange(sliderValue));
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
    setIsModalOpen(false);
    setPriceRangeString(e.target.value);
    setSliderValue(JSON.parse(e.target.value))
  }

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
                  />
              </Col>
              <Col span={24} className='d-flex justify-content-center'>
                <Slider
                  className='custom-slider'
                  range
                  min={0}
                  max={500}
                  step={5}
                  value={sliderValue}
                  onChange={handleSliderChange}
                />
              </Col>

              <Col span={24} className="d-flex justify-content-center">
                <Radio.Group 
                  onChange={handleRadioChange} 
                  value={priceRangeString}
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
                value={priceRangeString}
                className="custom-radio-group"
              > 
                <Space direction="vertical">
                  {areaRangeValueDetail.map((item, index) => (
                    <Radio 
                      key={index} value={item.value} 
                      className="label-light custom-radio mt-2"
                      >{item.label}</Radio>
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