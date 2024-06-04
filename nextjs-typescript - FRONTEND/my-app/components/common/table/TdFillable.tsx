import { Td } from "@chakra-ui/react";

interface FillableTdProps {
  children: string | JSX.Element;
  background: string | undefined;
  onClick: () => void;
  isDisabled: boolean;
}

function FillableTd({
  children,
  background,
  onClick,
  isDisabled,
}: FillableTdProps) {
  return (
    <>
      <Td
        userSelect={"none"}
        background={background}
        _hover={
          !isDisabled
            ? {
                cursor: "pointer",
                background: "gray.100",
                color: "teal.500",
              }
            : {}
        }
        border={"1px solid black"}
        textAlign="center"
        onClick={!isDisabled ? onClick : () => {}}
      >
        {children}
      </Td>
    </>
  );
}

export default FillableTd;
