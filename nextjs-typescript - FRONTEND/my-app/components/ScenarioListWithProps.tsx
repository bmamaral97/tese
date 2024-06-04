import { Scenario } from "../types";
import Link from "next/link";
import { Box, ListItem, UnorderedList } from "@chakra-ui/react";

function ScenarioListWithProps({ data }: { data: Scenario[] }) {
   
    if(!data) return <p>No scenarios data available...</p>

    return (
      <Box>
        <UnorderedList spacing={3}>
        {data.map((scenario, index) => {
          return (
            <ListItem key={index} fontSize='lg'>
                <Link href={"/scenarios/" + scenario.id}><a>Scenario name: {scenario.name}</a></Link>    
            </ListItem>
          )
        })}
        </UnorderedList>
      </Box>
    )
  }

export default ScenarioListWithProps