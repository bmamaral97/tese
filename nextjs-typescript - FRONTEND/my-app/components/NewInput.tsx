import { Button, Input, InputGroup, InputRightElement } from "@chakra-ui/react";
import { useState } from "react";

export interface NewInputProps {
  handleClick: (event: React.MouseEvent<HTMLButtonElement>) => void;
}

export function NewInput({ handleClick }: NewInputProps) {
  const [currentInput, setCurrentInput] = useState("");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCurrentInput(event.currentTarget.value);
  };

  return (
    <InputGroup size="md">
      <Input
        value={currentInput}
        onChange={handleChange}
        variant="flushed"
        pr="16px"
        type={"text"}
        placeholder="Enter formula"
      />
      <InputRightElement width="4.5rem">
        <Button
          h="1.75rem"
          size="sm"
          value={currentInput}
          onClick={handleClick}
        >
          Check
        </Button>
      </InputRightElement>
    </InputGroup>
  );
}
