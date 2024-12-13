import { Badge, Button, Card, Col, Form, Input, InputNumber, Radio, Row, Segmented, Select, Space, Spin, TreeSelect, message } from "antd";
import React, { useEffect, useState } from "react";
import { Editor } from '@tinymce/tinymce-react';
import { Link, useNavigate, useParams } from "react-router-dom";
import { Dayjs } from "dayjs";

import propertiesService from "../../../services/admin/properties.service";
import { CategoryNode, PropertyType } from "../../../commonTypes";
import * as standardizeData from '../../../helpers/standardizeData'
import UploadMultipleFile from "../../../components/admin/UploadMultipleFile/uploadMultipleFile";
import ExpireTimePicker from "../../../components/admin/ExpireTimePicker/expireTimePicker";
import { directionOptions, documentOptions, furnitureOptions, listingTypeOptions } from "../../../helpers/propertyOptions";
import NoPermission from "../../../components/admin/NoPermission/noPermission";
import propertyCategoriesService from "../../../services/admin/property-categories.service";
import { DefaultOptionType } from "antd/es/select";
import { IoBedOutline } from "react-icons/io5";
import { LuBath } from "react-icons/lu";
import { FaRegBuilding } from "react-icons/fa";
import { SlDirections } from "react-icons/sl";
import { DataNode } from "antd/es/tree";
import { formatTreeData } from "../../../helpers/treeHelper";

const EditProperty: React.FC = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [accessAllowed, setAccessAllowed] = useState(false);
  const [loading, setLoading] = useState(true);

  const [postType] = useState<string>('sell');
  const [propertyWidth, setPropertyWidth] = useState<number | null>(null);
  const [propertyLength, setPropertyLength] = useState<number | null>(null);
  const [priceMultiplier, setPriceMultiplier] = useState<number>(1);
  const [editorContent, setEditorContent] = useState<string>("");
  const [price, setPrice] = useState<number | null>(null);

  const [property, setProperty] = useState<any>(undefined);

  const [categoryTree, setCategoryTree] = useState<DataNode[] | undefined>(undefined);

  // data from child component
  const [expireDateTime, setExpireDateTime] = useState<Dayjs | null>(null);
  const [imageUrlToRemove, setImageUrlToRemove] = useState<string[]>([]);

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

        if (id) {
          const propertyResponse = await propertiesService.getSingleProperty(id);
          if (propertyResponse.status >= 200 && propertyResponse.status < 300) {        
            const propertyData = propertyResponse.data.data;
            const price = propertyData.propertyForSale?.salePrice || propertyData.propertyForRent?.rentPrice;
            setProperty(propertyData);
            console.log("Properties up data: ",propertyData)
            setPrice(price);

            price >= 1000 && setPriceMultiplier(1000); 

          } else {
            message.error(propertyResponse.message || 'Error occurred while fetching property data', 2);
          }
        } else {
          message.error('ID not provided, unable to fetch property data', 3);
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
  

  const handleEditorChange = (content: any, editor: any) => {
    const contentString = typeof content === 'string' ? content : '';
    setEditorContent(contentString);
  };

  // Child component functions  
  const handleImageUrlRemove = (imageUrl: string | undefined) => {
    // Check if imageUrl is not undefined and not already in the array
    if (imageUrlToRemove !== undefined && imageUrl !== undefined) {
      setImageUrlToRemove(prevImages => [...prevImages, imageUrl]);
    }
  }
  //

  const onFinishForm = async (data: any) => {
    try {
      const rooms = [
        data.bedrooms && { roomType: "Bedroom", quantity: data.bedrooms },
        data.bathrooms && { roomType: "Bathroom", quantity: data.bathrooms },
        data.kitchens && { roomType: "Kitchen", quantity: data.kitchens },
      ].filter((room) => room); 

      const propertyFormated = {
        ...data,
        "rooms": rooms,
        "balconyDirection": data.balconyDirection.replace(/ /g, "_"),
        "houseDirection": data.houseDirection.replace(/ /g, "_"),
        "accountId": "41dcd39e-a16f-4267-b698-af20fbcecede", 
        "description": editorContent,
        "availableFrom": "2024-12-30",
        "images": null
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
      
      const response = await propertiesService.updateProperty(formData, Number(id));

      if (response.status >= 200 && response.status < 300) {
        message.success('Property updated successfully!', 3);
      } else {
        const errorMessage = response.error || "An error occurred.";
        const errorDetails = response.details?.join(", ") || "No additional details.";
        message.error(`${errorMessage}. Details: ${errorDetails}`, 5);
      }
  
    } catch (error) {
      message.error("Error occurred while updating property.");
    }
  }

  return (
    <>
      {!loading ? (
        <>
        {accessAllowed ? (
          <div className="d-flex align-items-center justify-content-center"> 
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
                          label='Listing type:' 
                          name='listingType' 
                          style={{height: "4.5rem"}}
                          initialValue={property?.listingType}
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
                        label='Select property category' 
                        name={'categoryId'}
                        initialValue={property?.categoryId}
                      >
                        <TreeSelect
                          style={{ width: '100%' }}
                          dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
                          treeData={categoryTree}
                          placeholder="None by default"
                          treeDefaultExpandAll
                          treeLine
                        />
                      </Form.Item>
                    </Col>

                    <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                      <Form.Item 
                        label='City' 
                        name={['address', 'city']}  
                        initialValue={property.address?.split(', ')[3] || ''}
                      >
                        <Input placeholder={`Enter your city`} />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                      <Form.Item 
                        label='District' 
                        name={['address', 'district']}  
                        initialValue={property.address?.split(', ')[2] || ''}
                      >
                        <Input placeholder={`Enter your district`} />
                      </Form.Item>
                    </Col>
                    <Col sm={24} md={12} lg={8} xl={8} xxl={8}>
                      <Form.Item 
                        label='Ward' 
                        name={['address', 'ward']}  
                        initialValue={property.address?.split(', ')[1] || ''}
                      >
                        <Input placeholder={`Enter your ward`} />
                      </Form.Item>
                    </Col>
                    <Col span={24}>
                      <Form.Item 
                          label='Address' 
                          name={['address', 'streetAddress']}  
                          initialValue={property.address?.split(', ')[0] || ''}
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
                    <Form.Item 
                      label='Area'
                      name={'area'}
                    >
                      <InputNumber 
                        type="number" 
                        min={0} 
                        className="custom-number-input" 
                        placeholder="Enter width and height"
                        defaultValue={property.area}
                      />
                    </Form.Item>
                  </Col>

                  <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                    <Form.Item
                      label={`Property ${property.propertyForSale ? "sale" : "rental"} price`} 
                      name='price'
                      initialValue={price}
                    >
                      <InputNumber
                        min={0}
                        value={price}
                        type='number'
                        style={{width: "100%"}}
                        addonAfter={
                          <Select 
                            defaultValue={price && price >= 1000 ? "billion" : "million"} 
                            onChange={(value) => {
                              if (typeof value === 'number') {
                                setPrice(value);
                              }
                            }}
                          >
                            <Select.Option value="million">million</Select.Option>
                            <Select.Option value="billion">billion</Select.Option>
                          </Select>
                        } 
                        placeholder={`Please select property ${postType} price`} 
                      />
                    </Form.Item>
                  </Col>

                  {/* <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                    <Form.Item label={`Price per meter square`}>
                      <Input
                        disabled 
                        placeholder={`Select property ${postType} price and area to view`} 
                        style={{width: "100%"}}
                        value={(price * priceMultiplier) / property.area).toFixed(2) + }
                      />
                    </Form.Item>
                  </Col> */}
                  <Col span={24}>
                    <div className="line-two"></div>
                  </Col>
                  <Col sm={24} md={12} lg={12} xl={12} xxl={12}>
                    <Form.Item 
                      label={`Furnitures:`} 
                      name={'furniture'}  
                      initialValue={property?.furniture}
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
                      name='bedrooms'
                      initialValue={property.rooms.find((room: any) => room.roomType === 'Bedroom')?.quantity || 0}
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
                      initialValue={property.rooms.find((room: any) => room.roomType === 'Kitchen')?.quantity || 0}
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
                      initialValue={property.rooms.find((room: any) => room.roomType === 'Bathroom')?.quantity || 0}
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
                      initialValue={property?.totalFloor}
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
                      initialValue={property?.houseDirection}
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
                      initialValue={property?.balconyDirection}
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
                    <UploadMultipleFile uploadedImages={property?.imageUrls} setImageUrlRemove={handleImageUrlRemove}/>
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
                      initialValue={property?.title}
                    >
                      <Input type="text" id="title" required />
                    </Form.Item>
                  </Col>
                  <Col span={24}>
                    <Form.Item label={`Property ${postType}ing description:`}>
                      <Editor
                        id="description" 
                        initialValue={property?.description}               
                        onEditorChange={handleEditorChange}
                        apiKey='zabqr76pjlluyvwebi3mqiv72r4vyshj6g0u07spd34wk1t2' // hide
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
                    <Form.Item label="Post type:" name='postType' initialValue={property?.postType}>
                      <Radio.Group>
                        <Radio value="standard" className="label-light"> Standard </Radio>
                        <Radio value="premium" className="label-light"> Premium </Radio>
                        <Radio value="exclusive" className="label-light"> Exclusive </Radio>
                      </Radio.Group>
                    </Form.Item>
                  </Col>
                  <Col sm={24} md={24} lg={12} xl={12} xxl={12}>
                    <Form.Item label="Property status:" name='status' initialValue={property?.status === 'Active' ? 'active' : 'inactive'}>
                      <Radio.Group>
                        <Radio value="active">Active</Radio>
                        <Radio value="inactive">Inactive</Radio>
                      </Radio.Group>
                    </Form.Item>
                  </Col>

                  <Col span={24}>
                    <ExpireTimePicker 
                      onExpireDateTimeChange={(value) => setExpireDateTime(value)} 
                      expireTimeGiven={property?.expireTime}
                    />
                  </Col>'

  
                  <Col span={24}>
                    <Form.Item>
                      <Button className='custom-btn-main' type="primary" htmlType="submit">
                        Update
                      </Button>
                    </Form.Item>
                  </Col>
                </Row>
              </Card>
            </Form>
          </div>
        ) : (
          <NoPermission permissionType='access' />
        )}
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

export default EditProperty;