import { RoomType } from "../commonTypes";

// forSale => For Sale.  house => House
export const listingTypeFormatted = (word: string): string => {
  return word ? word.toLowerCase().startsWith('for') ? 
    `For ${word.slice(3)}` 
    : word.charAt(0).toUpperCase() + word.slice(1) : word;
};

// For Sale => forSale.  House => house
export const reverseListingType = (formattedWord: string): string => {
  if (!formattedWord) 
    return formattedWord;

  const [prefix, ...rest] = formattedWord.split(' ');

  return prefix.toLowerCase() === 'for' 
    ? `for${rest.map((word) => word.charAt(0).toUpperCase() + word.slice(1)).join('')}`
    : formattedWord.toLowerCase();
};

export const getRoomCount = (roomList: string[], type: RoomType) => {
  try {
    const normalizedType = type.toLowerCase() as RoomType;    
    const room = roomList.find((f) => f.toLowerCase().startsWith(`${normalizedType}-`));
    if (room) {
      const number = parseInt(room.split('-')[1], 10);
      return isNaN(number) ? null : number;
    }
    return null;
  } catch (error) {
    console.log('Error occurred in getRoomCount helper function:', error);
  }
};

export const convertLabelToPermission = (label: string): string => {
  const parts = label.toLowerCase().split(' ');
  const basePermission = parts.slice(0, -1).join('-');
  const action = parts[parts.length - 1].toLowerCase();
  return `${basePermission}_${action}`;
};

export const convertPermissionToLabels = (label: string): string => {
  const parts = label.toLowerCase().split(/[-_]/);
  const basePermission = parts.slice(0, -1).map((word, index) => index === 0 ? word.charAt(0).toUpperCase() + word.slice(1) : word).join(' ');
  const action = parts[parts.length - 1];
  return `${basePermission} ${action}`;
};  

const buildFormData = (formData: FormData, data: any, parentKey?: string) => {
  if (data && typeof data === 'object' && !(data instanceof Date) && !(data instanceof File) && !(data instanceof Blob)) {
    if (Array.isArray(data) && parentKey === 'images') {
      data.forEach((imageFile: any) => {
        formData.append(parentKey!, imageFile.originFileObj); // Ensure parentKey is defined
      });
    } else {
      Object.keys(data).forEach(key => {
        buildFormData(formData, data[key], parentKey ? `${parentKey}[${key}]` : key);
      });
    }
  } else {
    const value = data == null ? '' : data;
    formData.append(parentKey!, value);
  }
}

export const objectToFormData = (data: any) => {
  const formData = new FormData();
  buildFormData(formData, data);

  if (formData.has('images')) {
    formData.delete('images');
  }

  // Append new images
  if (data.images && data.images.length > 0) {
    data.images.forEach((imageFile: any) => {
      if (imageFile.originFileObj) {
        formData.append('images', imageFile.originFileObj);
      }
    });
  }

  return formData;
}

export const imagesToFormData = (imageData: any): FormData => {
  const formData = new FormData();

  if (imageData && imageData.length > 0) {
    imageData.forEach((imageFile: any) => {
      const file = imageFile.originFileObj || imageFile;
      if (file instanceof File) {
        formData.append('images', file);
      } else {
        console.error('Invalid file type detected:', file);
      }
    });
  }

  return formData;
};


export const capitalizeString = (s: string): string => {
  return s.charAt(0).toUpperCase() + s.slice(1);
};

export const getShortAddress = (address: string): string => {
  const parts = address.split(',').map(part => part.trim());
  return parts.slice(0, 2).join(', ');
};


export default objectToFormData