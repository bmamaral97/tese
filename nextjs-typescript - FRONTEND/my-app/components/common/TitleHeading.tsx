import { Heading } from "@chakra-ui/react"

interface TitleHeadingProps {
    title: string
}

function TitleHeading({ title }: TitleHeadingProps) {
    return (
        <Heading
            size='lg'
            m='12'
        >
            {title}
        </Heading>
    )
}

export default TitleHeading