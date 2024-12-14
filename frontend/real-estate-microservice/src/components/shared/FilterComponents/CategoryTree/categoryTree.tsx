import React, { useEffect, useState } from "react";
import { TreeSelect, message } from "antd";
import { useDispatch } from "react-redux";
import { setCategory, setIsLoading } from "../../../../redux/reduxSlices/filtersSlice";
import propertyCategoriesService from "../../../../services/admin/property-categories.service";
import { useNavigate, useLocation } from "react-router-dom";
import { formatTreeData } from "../../../../helpers/treeHelper";
import { CategoryNode } from "../../../../commonTypes";

import "./categoryTree.scss";
import { DataNode } from "antd/es/tree";

interface CategoryTreeProps {
  label?: string;
  width?: string;
  text?: string;
  userType: "admin" | "client";
}

const CategoryTree: React.FC<CategoryTreeProps> = ({
  label,
  text,
  width = "100%",
  userType,
}) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();

  const [categoryTitle, setCategoryTitle] = useState<string | undefined>();
  const [categoryTree, setCategoryTree] = useState<DataNode[] | undefined>();

  useEffect(() => {
    const fetchData = async () => {
      try {
        dispatch(setIsLoading(true));
        const response = await propertyCategoriesService.getCategoryTree();

        if (response.status >= 200 && response.status < 300) {
          const formattedTreeData = response.data.data.map((item: CategoryNode) =>
            formatTreeData(item)
          );
          setCategoryTree(formattedTreeData);
        } else {
          message.error(response.error, 3);
        }
      } catch (err: any) {
        if (err.response?.status === 401) {
          message.error("Unauthorized - Please log in to access this feature.", 3);
          navigate(`${userType === "admin" && userType}/auth/login`);
        } else {
          message.error("Error occurred while fetching data", 2);
          console.error("Error occurred:", err);
        }
      } finally {
        dispatch(setIsLoading(false));
      }
    };

    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleChange = (selectedNode: string) => {
    setCategoryTitle(selectedNode);
    dispatch(setCategory(selectedNode));

    const params = new URLSearchParams(location.search);
    params.set("categoryId", selectedNode);
    navigate(`${location.pathname}?${params.toString()}`);
  };

  return (
    <div className="category-filter">
      {label && <span style={{ marginBottom: ".5rem" }}>{label}</span>}
      <TreeSelect
        style={{ width }}
        value={categoryTitle}
        dropdownStyle={{ maxHeight: 400, overflow: "auto" }}
        treeData={categoryTree}
        placeholder={text || "Please select"}
        className="custom-tree-select"
        treeDefaultExpandAll
        onChange={handleChange}
        treeLine
      />
    </div>
  );
};

export default CategoryTree;
