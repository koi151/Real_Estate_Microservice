import React, { useEffect, useState } from 'react';
import { Button, Col, Row, Segmented, Select, message } from 'antd';
import { FaPlus } from "react-icons/fa6";
import Search from 'antd/es/input/Search';
import { IoFilter } from 'react-icons/io5';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/stores';
import { SegmentedValue } from 'antd/es/segmented';

import { setListingType, setKeyword, setStatus, setSorting, resetFilters } from '../../redux/reduxSlices/filtersSlice';

import './filterBox.scss';
import PriceRange from '../shared/FilterComponents/PriceRange/priceRange';
import { sortingOptionsAdmin } from '../../helpers/filterOptions';

interface FilterBoxProps {
  statusFilter?: boolean;
  createAllowed?: boolean;
  multipleChange?: boolean;
  priceRangeFilter?: boolean;
  checkedList?: string[] | undefined;
  categoryFilter?: boolean;
  createPostLink: string;
  resetFilterLink: string;
}

const FilterBox: React.FC<FilterBoxProps> = ({
  statusFilter,
  priceRangeFilter,
  categoryFilter,
  multipleChange,
  createPostLink,
  resetFilterLink,
  checkedList
}) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const { listingType, status } = useSelector((state: RootState) => state.filters);

  const currentRole = useSelector((state: any) =>
    state.adminUser.fullName ? 'admin' : 'client'
  );

  const [isFilterDetailVisible, setIsFilterDetailVisible] = useState<boolean>(true);

  // Default value for "Segmented"
  useEffect(() => {
    const defaultType = 'rent';
    dispatch(setListingType(defaultType));
    updateQueryParams('type', defaultType);
  }, [dispatch]);

  const updateQueryParams = (key: string, value: string) => {
    const params = new URLSearchParams(searchParams.toString());
    if (value) {
      params.set(key, value);
    } else {
      params.delete(key);
    }
    setSearchParams(params);
  };

  const handleStatusClick = (value: string) => {
    dispatch(setStatus(value));
    updateQueryParams("status", value);
  };

  const handleSortingChange = (value: string) => {
    const [sortKey, sortValue] = value.split('-');
    dispatch(setSorting({ sortKey, sortValue }));
    updateQueryParams("sort", `${sortKey}-${sortValue}`);
  };

  const handleResetFilters = () => {
    dispatch(resetFilters());
    setSearchParams({});
    navigate(resetFilterLink);
  };

  return (
    <>
      <div className='filter-box'>
        <div className='d-flex justify-content-end align-items-center'>
          <div className='filter-box__button-wrapper--right'>
            <Search
              className='search-box'
              placeholder="Search by title..."
              onSearch={(value) => {
                dispatch(setKeyword(value));
                updateQueryParams("title", value);
              }}
            />
            <Button
              className='filter-button d-flex align-items-center justify-content-center ml-1'
              onClick={() => setIsFilterDetailVisible(prev => !prev)}
            >
              Filters <IoFilter style={{ marginLeft: '.75rem' }} />
            </Button>
            <Link to={createPostLink} className='custom-link'>
              <Button className='add-new-button'>
                Add new <FaPlus />
              </Button>
            </Link>
          </div>
        </div>
      </div>

      <Segmented
        options={['For rent', 'For sale']}
        defaultValue={'For rent'}
        onChange={(value: SegmentedValue) => {
          if (typeof value === 'string') {
            const newValue =
              value === 'For rent' ? 'rent' :
              value === 'For sale' ? 'sale' : '';

            dispatch(setListingType(newValue));
            updateQueryParams("type", newValue);
          }
        }}
        className={`listing-type ${isFilterDetailVisible ? '' : 'fade-out'}`}
      />

      <div
        className={`filter-box__detail 
          ${isFilterDetailVisible ? '' : 'fade-out'} 
          ${listingType ? '' : 'mt-3'} 
        `}
      >
        <Row className='custom-row d-flex align-items-center' style={{ width: "100%" }}>
          {statusFilter && (
            <Col xxl={8} xl={8} lg={8}>
              <div className='status-filter'>
                <span>Filter by status:</span>
                <span className='status-filter__status-wrap mr-2'>
                  <br />
                  <Button
                    onClick={() => handleStatusClick('')}
                    className={`custom-btn ${!status ? 'active' : ''}`}
                  >
                    All
                  </Button>
                  <Button
                    onClick={() => handleStatusClick('active')}
                    className={`custom-btn ${status === 'active' ? 'active' : ''}`}
                  >
                    Active
                  </Button>
                  <Button
                    onClick={() => handleStatusClick('inactive')}
                    className={`custom-btn ${status === 'inactive' ? 'active' : ''}`}
                  >
                    Inactive
                  </Button>
                </span>
              </div>
            </Col>
          )}
          <Col xxl={8} xl={8} lg={8}>
            <div className='sorting-items'>
              <span>Sorting by: </span>
              <br />
              <Select
                placement='bottomLeft'
                placeholder="Choose sorting method"
                defaultValue={'position-desc'}
                onChange={handleSortingChange}
                options={sortingOptionsAdmin}
                className='sorting-items__select'
              />
            </div>
          </Col>
          {multipleChange && (
            <Col xxl={8} xl={8} lg={8}>
              <div className='multiple-change'>
                <span>Multiple change: </span>
                <Select
                  placement='bottomLeft'
                  placeholder="Choose change to apply"
                  options={[
                    { label: 'Active status', value: 'active' },
                    { label: 'Inactive status', value: 'inactive' },
                    { label: 'Position change', value: 'position' },
                    { label: 'Delete items', value: 'delete' },
                  ]}
                  className='multiple-change__select'
                />
              </div>
            </Col>
          )}
          {priceRangeFilter && (
            <Col xxl={8} xl={8} lg={8}>
              <PriceRange
                label='Price range:' width='80%'
                text='Select to apply'
              />
            </Col>
          )}
          <Col xxl={8} xl={8} lg={8}>
            <Button
              onClick={handleResetFilters}
              className='clear-filters'
              danger type='primary'>
              Clear filters
            </Button>
          </Col>
        </Row>
      </div>
    </>
  );
};

export default FilterBox;
