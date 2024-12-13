import { SelectProps } from "antd";
import { SegmentedLabeledOption, SegmentedOptions } from "antd/es/segmented";

export const documentOptions: SelectProps['options'] = [
  {value: "land use right certificate", label: "Land use right certificate"},
  {value: "sale contract", label: "Sale contract"},
  {value: "waiting for certificate", label: "Waiting for certificate"},
];

export const furnitureOptions: SelectProps['options'] = [
  {value: "FULL", label: "Full"},
  {value: "SIMPLE", label: "Simple"},
  {value: "NONE", label: "No furniture"},
];

export const directionOptions: SelectProps['options'] = [
  {value: "north", label: "North"},
  {value: "east", label: "East"},
  {value: "south", label: "South"},
  {value: "west", label: "West"},
  {value: "NORTH_EAST", label: "Northeast"},
  {value: "SOUTH_EAST", label: "Southeast"},
  {value: "NORTH_WEST", label: "Northwest"},
  {value: "SOUTH_WEST", label: "Southwest"},
];

export const listingTypeOptions: SegmentedOptions<string | number | SegmentedLabeledOption<string | number>> = [
  { value: 'sale', label: 'For sale' }, 
  { value: 'rent', label: 'For rent' }
];