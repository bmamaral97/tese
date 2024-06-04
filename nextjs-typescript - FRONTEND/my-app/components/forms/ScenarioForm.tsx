import {
  Box,
  Button,
  FormControl,
  FormErrorMessage,
  Input,
  InputGroup,
  InputRightElement,
  Text,
} from "@chakra-ui/react";
import * as Yup from "yup";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import Router from "next/router";
import { postPrivateDataVoid } from "../../utils/NetworkUtils";
import { User } from "../../contexts/auth";

interface ScenarioFormData {
  code: string;
}

interface PostScenario {
  code: string;
  instructor: string;
}

interface ScenarioFormProps {
  user: User;
}

function ScenarioForm({ user }: ScenarioFormProps) {
  const validationSchema = Yup.object().shape({
    code: Yup.string().required("Scenario code is required"),
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ScenarioFormData>({
    mode: "onBlur",
    resolver: yupResolver(validationSchema),
  });

  const onSubmit = async (data: ScenarioFormData) => {
    const scenario: PostScenario = {
      code: data.code,
      instructor: user.username,
    };
    await postScenario(scenario);
    alert("Posted new Scenario");
    reset();
    Router.replace(Router.asPath);
  };

  return (
    <Box>
      <Text>Enter a scenario code:</Text>
      <form autoComplete="off" onSubmit={handleSubmit(onSubmit)}>
        <FormControl isInvalid={!!errors?.code?.message} isRequired>
          <InputGroup>
            <Input
              variant="flushed"
              id="code-field"
              type="text"
              {...register("code")}
              placeholder="scenario/variables/phrases"
            />
            <InputRightElement width="4.5rem">
              <Button h="1.75rem" size="sm" type="submit">
                Submit
              </Button>
            </InputRightElement>
          </InputGroup>
          <FormErrorMessage>{errors?.code?.message}</FormErrorMessage>
        </FormControl>
      </form>
    </Box>
  );
}

const postScenario = async (data: ScenarioFormData) => {
  await postPrivateDataVoid("/api/scenarios", data);
};

export default ScenarioForm;
