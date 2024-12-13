import { Badge, Button, Card, Col, Form, Input, InputNumber, 
         Radio, Row, Segmented, Select, Space, Spin, TreeSelect, message } from "antd";
import React, { useEffect, useState } from "react";
import { Editor } from '@tinymce/tinymce-react';
import { Link, useNavigate } from "react-router-dom";
import dayjs, { Dayjs } from "dayjs";
import { DefaultOptionType } from "antd/es/select";

// Icons
import { SlDirections } from "react-icons/sl";
import { IoBedOutline } from "react-icons/io5";
import { LuBath } from "react-icons/lu";
import { FaRegBuilding } from "react-icons/fa";

// Services
import propertiesService from "../../../services/admin/properties.service";
import propertyCategoriesService from "../../../services/admin/property-categories.service";

// Components
// import GetAddress from "../../../components/admin/getAddress/getAddress";
import ExpireTimePicker from "../../../components/admin/ExpireTimePicker/expireTimePicker";
import UploadMultipleFile from "../../../components/admin/UploadMultipleFile/uploadMultipleFile";
import NoPermission from "../../../components/admin/NoPermission/noPermission";

import * as standardizeData from '../../../helpers/standardizeData'

import './create.scss'
import { directionOptions, documentOptions, furnitureOptions, listingTypeOptions } from "../../../helpers/propertyOptions";
import { DataNode } from "antd/es/tree";
import { CategoryNode } from "../../../commonTypes";
import { formatTreeData } from "../../../helpers/treeHelper";
// import GetAddress from "../../../components/getAddress/getAddress";
// import GetAddress from "../../../components/getAddress/getAddress";

const CreateProperty: React.FC = () => {
  const navigate = useNavigate();

  const [accessAllowed, setAccessAllowed] = useState(false);
  const [loading, setLoading] = useState<boolean>(true);

  const [price, setPrice] = useState<number | null>(null);
  const [propertyWidth, setPropertyWidth] = useState<number | null>(null);
  const [propertyLength, setPropertyLength] = useState<number | null>(null);
  const [priceMultiplier, setPriceMultiplier] = useState<number>(1);
  const [editorContent, setEditorContent] = useState<string>("");
  const [categoryTree, setCategoryTree] = useState<DataNode[] | undefined>(undefined);
  

  // data from child component
  const [expireDateTime, setExpireDateTime] = useState<Dayjs | null>(null);


  // fetch categories data 
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await propertyCategoriesService.getCategoryTree();
        console.log("Tree res:", response)

        if (response.status >= 200 && response.status < 300 && response.data.data) {
          const formattedTreeData = response.data.data.map((item: any) => formatTreeData(item));
          setCategoryTree(formattedTreeData);
          setAccessAllowed(true);
        } else {
          setAccessAllowed(false);
          message.error(response.error, 3);
        }
      } catch (err: any) {
        if (err.response && err.response.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching property categories data', 2);
          console.log('Error occurred:', err);
        }
      } finally {
        setLoading(false);
      }
    };
  
    fetchData();
  }, [navigate]);



  const selectPriceUnit = (
    <Select defaultValue="million" onChange={ (value) => setPriceMultiplier(value === 'million' ? 1 : 1000)}>
      <Select.Option value="million">million</Select.Option>
      <Select.Option value="billion">billion</Select.Option>
    </Select>
  );

  const handleEditorChange = (content: any) => {
    const contentString = typeof content === 'string' ? content : '';
    setEditorContent(contentString);
  };

  const onFinishForm = async (data: any) => {
    try {  
      const rooms = [
        data.bedrooms && { roomType: "Bedroom", quantity: data.bedrooms },
        data.bathrooms && { roomType: "Bathroom", quantity: data.bathrooms },
        data.kitchens && { roomType: "Kitchen", quantity: data.kitchens },
      ].filter((room) => room); 

      let postServciceIds = [];
      postServciceIds.push(expireDateTime ? expireDateTime.diff(dayjs(), 'day') < 6 
      ? 1 : expireDateTime.diff(dayjs(), 'day') < 14 ? 2 : 3 : 0);

      const propertyFormated = {
        ...data,
        "rooms": rooms,
        "accountId": "41dcd39e-a16f-4267-b698-af20fbcecede", //test
        "description": editorContent,
        "availableFrom": "2024-12-30",
        "images": null,
        "postServiceIds": postServciceIds
      };

      if (data.type === 'sale') {
          propertyFormated.propertyForSale = {
              salePrice: data.price,
          };
      } else {
          propertyFormated.propertyForRent = {
              rentalPrice: data.price,
          };
      }

      console.log("propertyFormated: ", propertyFormated)

      const formData = standardizeData.imagesToFormData(data.images);
      formData.append('property', JSON.stringify(propertyFormated))

      for (const [key, value] of formData.entries()) {
        console.log(`Key: ${key}, Value:`, value, '\n');
      }

      
      const response = await propertiesService.createProperty(formData);
      if (response.status === 201) {
        message.success("Property created successfully!", 3);
      } else {
        const errorMessage = response.error || "An error occurred.";
        const errorDetails = response.details?.join(", ") || "No additional details.";
        message.error(`${errorMessage}. Details: ${errorDetails}`, 5);
      }

    
    } catch (error) {
      message.error("Error occurred while creating a new property.");
      console.log("Error occurred:", error)
    }
  }
  
  return (
    <>
      { !loading ? (
        <>
          {/* { accessAllowed ? ( */}
            <div className="d-flex flex-column align-items-center justify-content-center"> 
              <Form 
                layout="vertical" 
                onFinish={onFinishForm}
                method="POST"
                encType="multipart/form-data"
                className="custom-form" 
              >
                <Badge.Ribbon text={<Link to="/admin/properties">Back</Link>} color="purple" className="custom-ribbon">
                  <Card 
                    title="Basic information"
                    className="custom-card" 
                  >
                    <Row gutter={16}>
                      <Col span={24} className="mb-5">
                        <Form.Item 
                          label='Choose listing type' 
                          name='type' 
                          initialValue={'sale'}
                          style={{height: "4.5rem"}}
                        >
                          <Segmented 
                            options={listingTypeOptions}                      
                            block 
                            className="custom-segmented"
                          />
                        </Form.Item>
                      </Col>
                      <Col span={24}>
                        <Form.Item
                          name={'categoryId'}
                          label='Select property category'
                          required
                        >
                          <TreeSelect
                            style={{ width: '100%' }}
                            dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                            treeData={categoryTree}
                            placeholder="Please select"
                            treeDefaultExpandAll
                            treeLine
                          />
                        </Form.Item>
                      </Col>

                      <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                        <Form.Item 
                            label='City' 
                            name={['address', 'city']}  
                            required
                          >
                        <Input placeholder={`Enter your city`} />
                      </Form.Item>
                      </Col>
                      <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                        <Form.Item 
                            label='District' 
                            required
                            name={['address', 'district']}  
                          >
                        <Input placeholder={`Enter your district`} />
                      </Form.Item>
                      </Col>
                      <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                        <Form.Item 
                          label='Ward' 
                          name={['address', 'ward']}  
                          required
                        >
                          <Input placeholder={`Enter your ward`} />
                        </Form.Item>
                      </Col>
                      <Col span={24}>
                        <Form.Item 
                            label='Address' 
                            name={['address', 'streetAddress']}  
                            required
                          >
                          <Input placeholder={`Enter your address`} />
                        </Form.Item>
                      </Col>
                    </Row>
                  </Card>
                </Badge.Ribbon>

                <Card title="Property information" className="custom-card" style={{marginTop: '2rem'}}>
                  <Row gutter={16}>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item label='Area' name={'area'} required>
                        <InputNumber 
                          // required
                          type="number" min={0} 
                          className="custom-number-input" 
                          placeholder="Enter area"
                          name="area"
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item label={`Property price`} name='price' required>
                        <InputNumber 
                          min={0}
                          type="number"
                          addonAfter={selectPriceUnit} 
                          placeholder={`Please select property price`}
                          onChange={(value) => {
                            if (typeof value === 'number') {
                              setPrice(value);
                            }
                          }}
                          style={{width: "100%"}}
                        />
                      </Form.Item>
                    </Col>
                    {/* <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item label={`Price per meter square`}>
                        <Input
                          disabled 
                          placeholder={`Select property price and area to view`} 
                          style={{width: "100%"}}
                          value={`$(properarea / propertyWidth)).toFixed(2)} million`}
                        />
                      </Form.Item>
                    </Col> */}
                    <Col span={24}>
                      <div className="line-two"></div>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={`Legal documents:`} 
                        name={'legalDocuments'}  
                      >
                        <Select
                          mode="tags"
                          style={{ width: '100%' }}
                          placeholder="Choose or add specific legal documents"
                          options={documentOptions}
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={`Furnitures:`} 
                        name={'furniture'}  
                      >
                        <Select
                          style={{ width: '100%' }}
                          placeholder="Furniture"
                          options={furnitureOptions}
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item   
                        label={
                          <Space className="d-flex align-items-center">
                            <span>Number of bedrooms:</span>
                            <IoBedOutline />
                          </Space>
                        } 
                        name="bedrooms"
                      >
                        <InputNumber 
                          min={0} type="number"
                          placeholder="Enter the number of bedrooms" 
                          style={{width: "100%"}} 
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item   
                        label={
                          <Space className="d-flex align-items-center">
                            <span>Number of kitchens:</span>
                            <IoBedOutline />
                          </Space>
                        } 
                        name='kitchens'
                      >
                        <InputNumber 
                          min={0} type="number"
                          placeholder="Enter the number of kitchens" 
                          style={{width: "100%"}} 
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={
                          <Space className="d-flex align-items-center">
                            <span>Number of bathrooms:</span>
                            <LuBath />
                          </Space>
                        }
                        name='bathrooms'
                      >
                        <InputNumber 
                          min={0} type="number"
                          placeholder="Enter the number of bathrooms" 
                          style={{width: "100%"}}
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={
                          <Space className="d-flex align-items-center">
                            <span>Number of floors:</span>
                            <FaRegBuilding />
                          </Space>
                        }                  
                        name={'totalFloor'}
                      >
                        <InputNumber 
                          min={0} type="number"
                          placeholder="Enter the number of floors" 
                          style={{width: "100%"}} 
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={
                          <Space className="d-flex align-items-center">
                            <span>House direction:</span>
                            <SlDirections />
                          </Space>
                        }      
                        name={'houseDirection'}
                      >
                        <Select
                          style={{ width: '100%' }}
                          placeholder="House direction"
                          options={directionOptions}
                        ></Select>
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                      <Form.Item 
                        label={
                          <Space className="d-flex align-items-center">
                            <span>Balcony direction:</span>
                            <SlDirections />
                          </Space>
                        }                  
                        name={'balconyDirection'}
                      >
                        <Select
                          style={{ width: '100%' }}
                          placeholder="Balcony direction"
                          options={directionOptions}
                        ></Select> 
                      </Form.Item>
                    </Col>
                    <Col span={24}>
                      <div className="line-two"></div>
                    </Col>
                    <Col span={24}>
                      <UploadMultipleFile />
                    </Col>
                  </Row>
                </Card>

                <Card title="Post information" className="custom-card" style={{marginTop: '2rem'}}>
                  <Row gutter={16}>
                    <Col span={24}>
                      <Form.Item 
                        label={<span>Post title <b className="required-txt">- required:</b></span>}
                        name='title'
                        required
                      >
                        <Input type="text" id="title" required />
                      </Form.Item>
                    </Col>
                    <Col span={24}>
                      <Form.Item label={`Property description:`}>
                        <Editor
                          id="description" 
                          value={editorContent}
                          onEditorChange={handleEditorChange}
                          apiKey='zabqr76pjlluyvwebi3mqiv72r4vyshj6g0u07spd34wk1t2'
                          init={{
                            toolbar_mode: 'sliding', 
                            plugins: ' anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount', 
                            toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | align lineheight | tinycomments | checklist numlist bullist indent outdent | emoticons charmap | removeformat', 
                            tinycomments_mode: 'embedded', tinycomments_author: 'Author name', mergetags_list: [ { value: 'First.Name', title: 'First Name' }, { value: 'Email', title: 'Email' }, ], 
                          }}
                        />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                      <Form.Item label="Post type:" name='packageType' initialValue={'standard'}>
                        <Radio.Group>
                          <Radio value="standard" className="label-light"> Standard </Radio>
                          <Radio value="premium" className="label-light"> Premium </Radio>
                          <Radio value="exclusive" className="label-light"> Exclusive </Radio>
                        </Radio.Group>
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                      <Form.Item label="Property status:" name='status' initialValue={'active'}>
                        <Radio.Group>
                          <Radio value="active">Active</Radio>
                          <Radio value="inactive">Inactive</Radio>
                        </Radio.Group>
                      </Form.Item>
                    </Col>

                    <Col span={24}>
                      <ExpireTimePicker 
                        onExpireDateTimeChange={(value) => setExpireDateTime(value)} 
                      />
                    </Col>

                    <Col span={24}>
                      <Form.Item>
                        <Button className='custom-btn-main' type="primary" htmlType="submit">
                          Submit
                        </Button>
                      </Form.Item>
                    </Col>
                  </Row>
                </Card>
              </Form>
            </div>
          {/* ) : (
            <NoPermission permissionType='access' />
          )} */}
        </>
      ) : (
        <div className='d-flex justify-content-center' style={{width: "100%", height: "100vh"}}>
          <Spin tip='Loading...' size="large">
            <div className="content" />
          </Spin>
        </div>
      )}
    </>
  )
}

export default CreateProperty;

