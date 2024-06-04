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
import { ExerciseFormProps, FileFormData } from "../../types";
import { postPrivateFile } from "../../utils/NetworkUtils";
import { SyntheticEvent, useState } from "react";

function FileForm({ user }: ExerciseFormProps) {
  const [selectedFile, setSelectedFile] = useState<FileFormData>({
    fileName: "",
    file: null,
  });

  const fileFormSchema = Yup.object().shape({
    file: Yup.mixed().test("required", "You need to provide a file", (file) => {
      // return file && file.size <-- u can use this if you don't want to allow empty files to be uploaded;
      if (file) return true;
      return false;
    }),
    /*.test("fileSize", "The file is too large", (file) => {
            //if u want to allow only certain file sizes
            return file && file.size <= 200000000000000;
          })*/
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<FileFormData>({
    mode: "onBlur",
    resolver: yupResolver(fileFormSchema),
  });

  const onSubmit = async () => {
    if (selectedFile !== null) {
      console.log(selectedFile);
      await postFile(selectedFile, user.username);
      alert("Uploaded scenarios and exercises to the system.");
    }

    reset();
    Router.replace(Router.asPath);
  };

  const handleFileChange = (element: HTMLInputElement) => {
    const file: FileList | null = element.files;
    if (file === null) {
      return "file is null";
    }
    setSelectedFile({
      fileName: file[0].name,
      file: file[0],
    });
  };

  return (
    <Box>
      <Text>Select a file to upload:</Text>
      <form
        autoComplete="off"
        onSubmit={handleSubmit(onSubmit)}
        encType="multipart/form-data"
      >
        <FormControl isInvalid={!!errors?.file?.message} isRequired>
          <InputGroup>
            <Input
              variant="flushed"
              id="file-field"
              type="file"
              {...register("file")}
              placeholder="scenario/phrase/formula"
              onChange={(e: SyntheticEvent) =>
                handleFileChange(e.currentTarget as HTMLInputElement)
              }
            />
            <InputRightElement width="4.5rem">
              <Button h="1.75rem" size="sm" type="submit">
                Submit
              </Button>
            </InputRightElement>
          </InputGroup>
          <FormErrorMessage>{errors?.file?.message}</FormErrorMessage>
        </FormControl>
      </form>
    </Box>
  );
}

const postFile = async (data: FileFormData, username: string) => {
  if (data.file) {
    await postPrivateFile("/api/files", data.file, username);
  }
};

export default FileForm;
