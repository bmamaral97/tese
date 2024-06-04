import { Box, Button, ButtonGroup, Text } from "@chakra-ui/react";
import { useState } from "react";

interface FormulaTyperProps {
    data: string[],
    onSubmit: (event: React.MouseEvent<HTMLButtonElement>) => void,
    onClick?: ((event: React.MouseEvent<HTMLButtonElement>) => void),
}
// const utf8 = require('utf8');
const operators = ['\u21d2', '\u21d4', '\u00ac', '\u2227', '\u2228', '(', ')']

const FormulaTyper: React.FC<FormulaTyperProps> = (props) => {
    const [value, setValue] = useState("")
    const { data, onSubmit } = props

    const handleClick = props.onClick || ((event: React.MouseEvent<HTMLButtonElement>) => {
        const eventTarget: string = (event.currentTarget as HTMLButtonElement).value
        setValue(value.concat(eventTarget));
    })

    const handleSubmit = (event: React.MouseEvent<HTMLButtonElement>) => {
        setValue('');
        onSubmit(event)
    }

    const clearLastChar = () => {
        setValue(value.substring(0, value.length - 1))
    }

    return (
        <Box boxShadow='xs' p='5' rounded='md' bg='white'>
            <ButtonGroup spacing="1" variant="solid">
                {data.map((char) => {
                    return (
                        <Button key={char} onClick={handleClick} value={char}>{char}</Button>
                    );
                })}
                {operators.map((op) => {
                    return (
                        <Button key={op} onClick={handleClick} value={op}>{op}</Button>
                    );
                })}
                <Button onClick={clearLastChar}>C</Button>
                <Button onClick={handleSubmit} value={value}>S</Button>
            </ButtonGroup>
            <Text mb='8px'>Value: {value}</Text>
        </Box >
    )
}

export default FormulaTyper