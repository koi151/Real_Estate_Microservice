import { DataNode } from "antd/es/tree";
import { CategoryNode } from "../commonTypes";

interface CustomDataNode extends DataNode {
  value: string; 
}

export const formatTreeData = (node: CategoryNode): CustomDataNode => ({
  key: node.id.toString(),
  value: node.id.toString(), 
  title: node.title,
  children: node.children?.map(formatTreeData) || [], // Recursive format children
});
