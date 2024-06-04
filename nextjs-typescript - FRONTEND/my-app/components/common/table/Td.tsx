import { Td } from "@chakra-ui/react"

interface TdProps {
    children: string | JSX.Element
}

const BaseTd = ({ children }: TdProps) => {
    return (
        <>
            <Td
                 userSelect={"none"}
                 background="gray.100"
                 border={"1px solid black"}
            >
                {children}
            </Td>
        </>
    )
}

export default BaseTd