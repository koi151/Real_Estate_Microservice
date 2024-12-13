import { DataNode } from "antd/es/tree";
import { CategoryNode } from "../commonTypes";

export const formatTreeData = (node: CategoryNode): DataNode => ({
  key: node.id.toString(),  
  title: node.title,
  children: node.children?.map((child: CategoryNode) => formatTreeData(child)) || [],
});