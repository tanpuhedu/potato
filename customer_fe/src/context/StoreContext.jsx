import { createContext } from "react";
import { food_list } from "../assets/assets";

// Tạo context
export const StoreContext = createContext(null);

const StoreContextProvide = (props) => {
  const contextValue = {
    food_list,
  };

  return (
    <StoreContext.Provider value={contextValue}>
      {props.children} 
    </StoreContext.Provider>
  );
};

// Xuất default để import ở file main.jsx dùng được
export default StoreContextProvide;
