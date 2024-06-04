import { Th } from "@chakra-ui/react";
import Formula from "../Formula";

interface ThProps {
  children: string | JSX.Element;
}

const BaseTh = ({ children }: ThProps) => {
  return (
    <>
      <Th
        textAlign="center"
        border={"1px solid black"}
        width="max-content"
        userSelect={"none"}
      >
        {typeof children == "string" ? (
          <Formula formula={children}></Formula>
        ) : (
          children
        )}
      </Th>
    </>
  );
};

export default BaseTh;
