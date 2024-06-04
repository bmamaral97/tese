import {
  Box,
  Flex,
  Avatar,
  HStack,
  Link,
  IconButton,
  Button,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  useDisclosure,
  useColorModeValue,
  Stack,
} from "@chakra-ui/react";
import { CloseIcon, HamburgerIcon, ArrowLeftIcon } from "@chakra-ui/icons";
import { useAuth } from "../contexts/auth";
import { MenuGroup } from "@chakra-ui/react";
import Router from "next/router";
import { MenuDivider } from "@chakra-ui/react";

const Links = [
  {
    to: "/",
    name: "Home",
  },
  {
    to: "/exercises",
    name: "Exercises",
  },
];

export default function Navbar() {
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { user, isAuthenticated, logout, isAdmin, loading } = useAuth();

  const bgColor = useColorModeValue("gray.200", "gray.700");

  if (!loading) {
    return (
      <Box bg={bgColor} px={4}>
        <Flex h={"10vh"} alignItems={"center"} justifyContent={"space-between"}>
          <IconButton
            size={"md"}
            icon={isOpen ? <CloseIcon /> : <HamburgerIcon />}
            aria-label={"Open Menu"}
            display={{ md: "none" }}
            onClick={isOpen ? onClose : onOpen}
          />
          <HStack spacing={8} alignItems={"center"}>
            <Box color="blue">Learning Logic</Box>
            <HStack
              as={"nav"}
              spacing={4}
              display={{ base: "none", md: "flex" }}
            >
              {Links.map((link) => (
                <Link
                  key={link.name}
                  px={2}
                  py={1}
                  rounded={"md"}
                  _hover={{
                    textDecoration: "none",
                    bg: bgColor,
                  }}
                  href={link.to}
                >
                  {link.name}
                </Link>
              ))}
            </HStack>
          </HStack>
          <Flex alignItems={"center"}>
            {/*<Box px={5}>
                            <Button onClick={() => {
                                Router.back()
                            }}>
                                <ArrowLeftIcon />
                            </Button>
                        </Box>*/}
            {isAuthenticated ? (
              <Menu>
                <MenuButton
                  as={Button}
                  rounded={"full"}
                  variant={"link"}
                  cursor={"pointer"}
                  minW={0}
                >
                  {isAdmin ? (
                    <Avatar size={"sm"} name={"Admin"} src={""} />
                  ) : (
                    <Avatar size={"sm"} name={"User"} src={""} />
                  )}
                </MenuButton>
                <MenuList>
                  <MenuGroup title={"Logged in as " + user?.username}>
                    <MenuDivider />
                    {isAdmin ? (
                      <MenuItem onClick={() => Router.push("/admin")}>
                        Admin
                      </MenuItem>
                    ) : null}
                    <MenuItem onClick={() => logout()}>Logout</MenuItem>
                  </MenuGroup>
                </MenuList>
              </Menu>
            ) : (
              <Button
                size={"sm"}
                colorScheme="blue"
                onClick={() => Router.push("/login")}
              >
                Login
              </Button>
            )}
          </Flex>
        </Flex>

        {isOpen ? (
          <Box pb={4} display={{ md: "none" }}>
            <Stack as={"nav"} spacing={4}>
              {Links.map((link) => (
                <Link
                  key={link.name}
                  px={2}
                  py={1}
                  rounded={"md"}
                  _hover={{
                    textDecoration: "none",
                    bg: bgColor,
                  }}
                  href={link.to}
                >
                  {link.name}
                </Link>
              ))}
            </Stack>
          </Box>
        ) : null}
      </Box>
    );
  } else {
    return (
      <Box bg={bgColor} px={4}>
        <Flex h={"10vh"} alignItems={"center"} justifyContent={"space-between"}>
          <HStack spacing={8} alignItems={"center"}>
            <Box color="blue">Learning Logic</Box>
            <HStack
              as={"nav"}
              spacing={4}
              display={{ base: "none", md: "flex" }}
            >
              {Links.map((link) => (
                <Link
                  key={link.name}
                  px={2}
                  py={1}
                  rounded={"md"}
                  _hover={{
                    textDecoration: "none",
                    bg: bgColor,
                  }}
                  href={link.to}
                >
                  {link.name}
                </Link>
              ))}
            </HStack>
          </HStack>
        </Flex>
      </Box>
    );
  }
}
