import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { Badge, Breadcrumb, Button, Col, Input, Pagination, 
         PaginationProps, Row, Select, Skeleton, Tooltip,  message } from 'antd';
import { RiSearchLine } from "react-icons/ri";
import { RootState } from '../../../redux/stores';

import getPriceUnit from '../../../helpers/getPriceUnit';


import { PaginationObj, ClientProperty } from '../../../commonTypes';
import RoomCountTooltip from '../../../components/shared/Counters/RoomCounter/roomCount';

import HTMLContent from '../../../components/client/HTMLContent/HTMLContent';
import FilterBoxSlide from '../../../components/shared/FilterComponents/FilterBoxSlide/filterBoxSlide';
import PriceRange from '../../../components/shared/FilterComponents/PriceRange/priceRange';
import AreaRange from '../../../components/shared/FilterComponents/AreaRange/areaRange';

import { setKeyword, setSorting } from '../../../redux/reduxSlices/filtersSlice';
import { setPermissions } from '../../../redux/reduxSlices/adminUserSlice'; 

import { sortingOptionsClient } from '../../../helpers/filterOptions';

import './properties.scss';
import propertiesServiceClient from '../../../services/client/properties.service';
import { getShortAddress } from '../../../helpers/standardizeData';

const Properties: React.FC = () => {

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const filters = useSelector((state: RootState) => state.filters);

  const [loading, setLoading] = useState(true);
  const [propertyList, setPropertyList] = useState<ClientProperty[]>([]);
  const [error, setError] = useState<string | null>(null); 
  const [propertyCount, setPropertyCount] = useState<number>(0);

  const { listingType, keyword, status, category, direction, 
          priceRange, areaRange, sorting, bedrooms, bathrooms } = filters;

  const [currentPage, setCurrentPage] = useState<number>(1);
  
  const [paginationObj, setPaginationObj] = useState<PaginationObj>({
    maxPageItems: 10, 
    totalItems: undefined,
    totalPages: undefined,
    currentPage: 1,
  })

  const onPageChange: PaginationProps['onChange'] = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  // fetch properties data
  useEffect(() => {
    const fetchData = async() => {
      try {
        setLoading(true);

        const query = location.search;

        const response = await propertiesServiceClient.getAllProperties(query);
        console.log('RES PROPERTIES:', response);

        if (response?.status >= 200 && response?.status < 300) {
          setPropertyList(response.data.data);
          setPaginationObj({
            maxPageItems: response.data.maxPageItems,
            totalItems: response.data.totalItems,
            totalPages: response.data.totalPages,
            currentPage: response.data.currentPage
          });
          
        } else if (response?.status === 401) {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error(response.desc || 'Failed to fetch properties.', 4);
        }

      } catch (err: any) {
        if ((err.response && err.response.status === 401) || err.message === 'Unauthorized') {
          message.error('Unauthorized - Please log in to access this feature.', 3);
          navigate('/admin/auth/login');
        } else {
          message.error('Error occurred while fetching properties data.', 2);
          setError('No property found.');
          console.error('Error occurred:', error);
        }
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [navigate, location.search])

  const handleSortingChange = (value: string) => {
    const [sortKey, sortValue] = value.split('-');
    dispatch(setSorting({ sortKey, sortValue }));
  };

  const renderImages = (imageUrls: string[] = []) => {
    if (imageUrls.length === 1) {
      return (
        <div className="post-wrapper__images single-image">
          <div className="image-full-wrap">
            <img src={imageUrls[0]} alt="property img" />
          </div>
        </div>
      );
    }
  
    if (imageUrls.length === 2) {
      return (
        <div className="post-wrapper__images two-images">
          {imageUrls.map((url, index) => (
            <div key={index} className="image-half-wrap">
              <img src={url} alt={`property img ${index + 1}`} />
            </div>
          ))}
        </div>
      );
    }
  
    if (imageUrls.length === 3) {
      return (
        <div className="post-wrapper__images three-images">
          <div className="image-half-wrap">
            <img src={imageUrls[0]} alt="1st property img" />
          </div>
          <div className="image-stack-wrap">
            {imageUrls.slice(1).map((url, index) => (
              <div key={index} className="image-quarter-wrap">
                <img src={url} alt={`${index + 2}nd property img`} />
              </div>
            ))}
          </div>
        </div>
      );
    }
  
    return (
      <div className="post-wrapper__images default-layout">
        <div className="image-primary-wrap">
          <img src={imageUrls[0] ?? ''} alt="1st property img" />
        </div>
        <div className="images-group">
          <div className="images-group__secondary-wrap">
            <img src={imageUrls[1] ?? ''} alt="2nd property img" />
          </div>
          <div className="images-group__smaller-group">
            <div className="images-group__smaller-group--image-wrap">
              <img src={imageUrls[2] ?? ''} alt="3rd property img" />
            </div>
            <div className="images-group__smaller-group--image-wrap">
              <img src={imageUrls[3] ?? ''} alt="4th property img" />
            </div>
          </div>
        </div>
      </div>
    );
  };
    
    

  
  return (
    <>
      {/* {accessAllowed ? ( */}
        <>
          <Input
            addonBefore={<RiSearchLine />}
            className='search-box search-box-custom'
            placeholder="Search by title..."
            onChange={(e) => dispatch(setKeyword(e.target.value))}
            suffix={<Button type="primary">Search</Button>}

          />
            <FilterBoxSlide slickWidth='70%' userType='client'/>
            
            <div className='line' style={{marginTop: "1.75rem"}} /> 

            <div className='d-flex' style={{width: "100%"}}>
              <div style={{width: "70%"}}>
                <div className='title-wrapper mt-2 p-0 pt-3'>
                  <Breadcrumb
                    className='mt-1 mb-1'
                    items={[
                      { title: <Link to="/">Home</Link> },
                      { title: <Link to="/properties">All real estate nationwide</Link> },
                    ]}
                  />
                  <h1 className="main-content-title mt-2">Buy and sell real estate nationwide</h1>
                  <div className='d-flex justify-content-between align-items-center mt-4'>
                    {loading ? (
                      <span>Data is loading, please wait for a moment...</span>
                    ) : (
                      <span>Currently have {paginationObj.totalItems} properties</span>
                    )}
                    <div className='sorting-items'>
                      <Select
                        placement='bottomLeft'
                        placeholder="Choose sorting method"
                        defaultValue={'Sort by default'}
                        onChange={handleSortingChange}
                        options={sortingOptionsClient}
                        className='sorting-items__select'
                        style={{width: "20rem"}}
                      />
                    </div>
                  </div>
                </div>  

              {error ? (
                <div>{error}</div>
              ) : (
                <>
                <Skeleton loading={loading} active style={{ padding: '3.5rem' }}>
                  <div className='d-flex flex-column'>
                    {propertyList?.length > 0 ? (
                      propertyList.map((property, index) => {
                        return (
                          <div 
                            className='post-wrapper' 
                            onClick={() => navigate(`/properties/detail/${property.propertyId}`)}
                            key={index}
                          >
                            <div className='post-wrapper__images' onClick={() => navigate(`/properties/detail/${property.propertyId}`)}>
                              {property.postType === 'premium' || property.postType === 'exclusive' ? (
                                <Badge.Ribbon
                                  text={
                                    <Tooltip
                                      title={property.postType ? `${property.postType.charAt(0).toUpperCase() + property.postType.slice(1)} post` : ''}
                                    >
                                      <div style={{ display: 'inline-block', transform: 'scaleX(-1)' }}>
                                        {property.postType.charAt(0).toUpperCase() + property.postType.slice(1)}
                                      </div>
                                    </Tooltip>
                                  }
                                  color={property.postType === 'premium' ? 'purple' : 'gold'}
                                  className='custom-ribbon-2'
                                  style={{ position: 'absolute', top: '.5rem', left: '-.5rem', transform: 'scaleX(-1)' }}
                                >
                                  {renderImages(property.imageUrls)}
                                </Badge.Ribbon>
                              ) : (
                                renderImages(property.imageUrls)
                              )}
                            </div>


                            <div className='post-wrapper__content'>
                              <div className='post-wrapper__content--title'>
                                {property.title}
                              </div>
                              <div className='d-flex mt-3 mb-3 align-items-center'>
                                {property?.rentalPrice || property?.salePrice ? (
                                  <>
                                    <span className='post-wrapper__content--price'>
                                      {((property.rentalPrice || property.salePrice)! > 1000
                                        ? (property.rentalPrice || property.salePrice)! / 1000
                                        : property.rentalPrice || property.salePrice
                                      )
                                        .toFixed(2)
                                        .replace(/\.00$/, '')}
                                    </span>
                                    <span className='post-wrapper__content--price-unit'>
                                      {getPriceUnit(property.rentalPrice || property.salePrice)}
                                    </span>
                                  </>
                                ) : (
                                  <span className='post-wrapper__content--price'>Negotiable price</span>
                                )}


                                <span className='post-wrapper__content--area'>
                                  {property?.area}
                                  <span style={{ marginLeft: '.3rem' }}>m²</span>
                                </span>
                                {property?.area && (property.rentalPrice || property.salePrice) && (
                                  <span className='post-wrapper__content--price-per-area-unit'>
                                    {((property.rentalPrice || property.salePrice)! / property.area).toFixed(2)}
                                    <span style={{ marginLeft: '.3rem' }}>
                                      {getPriceUnit(property.rentalPrice || property.salePrice).slice(0, 3)}/m²
                                    </span>
                                  </span>
                                )}
                                {property.rooms
                                  .filter((room: any) => room.roomType === 'Bedroom' || room.roomType === 'Bathroom')
                                  .length > 0 && (
                                    <span className='post-wrapper__content--features'>
                                      {property.rooms
                                        .filter((room: any) => room.roomType === 'Bedroom' || room.roomType === 'Bathroom')
                                        .map((room: any) => (
                                          <RoomCountTooltip 
                                            key={room.roomType} 
                                            roomType={room.roomType} 
                                            quantity={room.quantity} 
                                          />
                                        ))}
                                    </span>
                                  )}

                                {property.address ? (
                                  <Tooltip title={property.address}>
                                    <span className='post-wrapper__content--position ml-2'>
                                      {getShortAddress(property.address)}
                                    </span>
                                  </Tooltip>
                                ) : (
                                  'No information'
                                )}
                              </div>

                              {property.description && (
                                <span className='post-wrapper__content--description'>
                                  <HTMLContent htmlContent={property.description} />
                                </span>
                              )}
                            </div>

                            <div className='line' style={{width: "100%"}}></div>
                              <Row>
                                <Col span={24}>
                                  <div className='lower-content'>
                                    <div className='lower-content--date-created'>
                                      Created at: {property.createdDate ? new Date(property.createdDate).toLocaleString() : 'No data'}
                                    </div>
                                  </div>
                                </Col>
                              </Row>
                          </div>
                        );
                      })
                    ) : (
                      <>Loading...</>
                    )}
                  </div>
                  
                </Skeleton>
                <Skeleton loading={loading} active style={{ padding: '3.5rem' }}></Skeleton>
                <Skeleton loading={loading} active style={{ padding: '3.5rem' }}></Skeleton>
                </>
              )}            
            </div>
            <div className='d-flex flex-column align-items-end' style={{ width: "30%" }}>
              <PriceRange width='100%' modelDisable/>
              <AreaRange width='100%' modelDisable/>
            </div>
            </div>

            <Pagination
              className='mt-5'
              showQuickJumper
              pageSize={paginationObj.maxPageItems}
              onChange={onPageChange}
              defaultCurrent={paginationObj.currentPage}
              total={paginationObj.totalItems}
            />
        </>
      {/* ) : (
        <NoPermission permissionType='view' />
      )} */}
    </>
  );
};

export default Properties;
