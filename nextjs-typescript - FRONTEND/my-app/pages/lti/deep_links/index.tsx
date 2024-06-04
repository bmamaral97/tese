import { Button, List, ListItem } from "@chakra-ui/react";
import Head from "next/head";
import { useState } from "react";
import DefaultContainer from "../../../components/common/DefaultContainer";
import TitleHeading from "../../../components/common/TitleHeading";

interface Item {
  title: String,
  text: String,
  url: String,
  documentTarget: String,
  type: String,
  mediaType: String,
}

interface PageProps {

}

const DeepLinksRedirectPage = ({ }: PageProps) => {
  const defaultItem: Item = {
    title: "default title",
    text: "text",
    url: "default url",
    documentTarget: "",
    type: "",
    mediaType: ""
  }

  const [items, setItems] = useState<Item[]>([defaultItem])

  return (
    <>
      <Head>
        <title>Deep Links</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>

      <DefaultContainer>
        <TitleHeading title="Deep Linking Page" />

        <List mb={5}>
          {items.map(item => {
            return <ListItem>{item.title} - {item.url}</ListItem>
          })}
        </List>

        <Button mb={5}>
          Add Item
        </Button>

        <Button mb={5}>
          Remove Item
        </Button>

        <Button mb={5}>
          Save
        </Button>

      </DefaultContainer>
    </>
  );
};


export default DeepLinksRedirectPage;