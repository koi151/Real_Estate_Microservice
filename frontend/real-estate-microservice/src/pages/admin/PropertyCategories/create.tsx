import React, { useEffect, useState } from "react";
import { Editor } from '@tinymce/tinymce-react';
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { Badge, Button, Card, Col, Form, Input, Radio, Row, Spin, TreeSelect, message } from "antd";
import type { DataNode } from 'antd/es/tree';

import propertyCategoriesService from "../../../services/admin/property-categories.service";
import { CategoryNode, PropertyCategoryType } from "../../../commonTypes";
import * as standardizeData from '../../../helpers/standardizeData'
import NoPermission from "../../../components/admin/NoPermission/noPermission";
import UploadMultipleFile from "../../../components/admin/UploadMultipleFile/uploadMultipleFile";
import { formatTreeData } from "../../../helpers/treeHelper";

const CreatePropertyCategory: React.FC = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const [accessAllowed, setAccessAllowed] = useState(true);
  const [loading, setLoading] = useState<boolean>(true);
  const [editorContent, setEditorContent] = useState<string>("");
  const [category] = useState<PropertyCategoryType | undefined>(undefined);
  const [categoryTree, setCategoryTree] = useState<DataNode[] | undefined>(undefined);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await propertyCategoriesService.getCategoryTree();

        if (response.status >= 200 && response.status < 300 && response.data.data) {
          const formattedTreeData = response.data.data.map((item: CategoryNode) => formatTreeData(item));
          setCategoryTree(formattedTreeData);
          setAccessAllowed(true);
        } else {
          message.error(response.desc || 'Failed to fetch category data', 3);
        }
      } catch (err: any) {
        if (err.response?.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching data', 2);
          console.error('Error occurred:', err);
        }
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [dispatch, navigate]);

  const handleEditorChange = (content: string | undefined) => {
    setEditorContent(content || '');
  };

  const onFinishForm = async (data: any) => {
    try {
      const propertyCategory = {
        title: data.title,
        description: editorContent,
        status: data.status,
        parentCategoryId: data.parentCategoryId || null,
      };

      const formData = standardizeData.imagesToFormData(data.images);
      formData.append('propertyCategory', JSON.stringify(propertyCategory));

      const response = await propertyCategoriesService.createCategory(formData);

      if (response.status >= 200 && response.status < 300) {
        message.success('Property category created successfully!', 3);
        navigate("/admin/property-categories");
      } else {
        message.error('Error occurred while creating category', 3);
      }
    } catch (error) {
      console.error("Error occurred while creating category:", error);
      message.error("Error occurred while creating category.");
    }
  };

  if (loading) {
    return (
      <div className='d-flex justify-content-center' style={{width: "100%", height: "100vh"}}>
        <Spin tip='Loading...' size="large">
          <div className="content" />
        </Spin>
      </div>
    );
  }

  if (!accessAllowed) {
    return <NoPermission permissionType='access' />;
  }

  return (
    <div className="d-flex align-items-center justify-content-center">
      <Form
        layout="vertical"
        onFinish={onFinishForm}
        method="POST"
        encType="multipart/form-data"
        className="custom-form"
      >
        <Badge.Ribbon text={<Link to="/admin/property-categories">Back</Link>} color="purple" className="custom-ribbon">
          <Card
            title="Create property category"
            className="custom-card"
            style={{marginTop: '2rem'}}
            extra={<Link to="/admin/property-categories">Back</Link>}
          >
            <Row gutter={16}>
              <Col span={24}>
                <Form.Item
                  label={<span>Property category name</span>}
                  name='title'
                  required
                >
                  <Input type="text" id="title" required />
                </Form.Item>
              </Col>
              <Col span={24}>
                <Form.Item
                  label='Parent category'
                  name='parentCategoryId'
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
              <Col span={24}>
                <Form.Item label="Property category description:">
                  <Editor
                    id="description"
                    onEditorChange={handleEditorChange}
                    apiKey={process.env.REACT_APP_API_TINYMCE}
                    init={{
                      toolbar_mode: 'sliding',
                      plugins: 'anchor autolink charmap codesample emoticons image link lists media searchreplace table visualblocks wordcount',
                      toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table mergetags | align lineheight | tinycomments | checklist numlist bullist indent outdent | emoticons charmap | removeformat',
                      tinycomments_mode: 'embedded',
                      tinycomments_author: 'Author name',
                      mergetags_list: [
                        { value: 'First.Name', title: 'First Name' },
                        { value: 'Email', title: 'Email' }
                      ],
                    }}
                  />
                </Form.Item>
              </Col>
              <Col sm={24} md={24} lg={10} xl={10} xxl={10}>
                <Form.Item label="Category status:" name='status' initialValue={'active'}>
                  <Radio.Group>
                    <Radio value="active">Active</Radio>
                    <Radio value="inactive" className="ml-3">Inactive</Radio>
                  </Radio.Group>
                </Form.Item>
              </Col>
              <Col sm={24} md={24} lg={14} xl={14} xxl={14}>
                <UploadMultipleFile uploadedImages={Array.isArray(category?.imageUrls) ? category?.imageUrls : undefined}/>
              </Col>
              <Col span={24}>
                <Form.Item>
                  <Button className='custom-btn-main' type="primary" htmlType="submit">
                    Create
                  </Button>
                </Form.Item>
              </Col>
            </Row>
          </Card>
        </Badge.Ribbon>
      </Form>
    </div>
  );
};

export default CreatePropertyCategory;