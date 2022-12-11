<script setup lang='ts'>
import { ref, watch } from 'vue';
const ChatList = ref<Array<any>>([])
const sendMsg = ref()
const myName = ref()
let ws: WebSocket

watch(() => ChatList.value.length, () => {
    setTimeout(() => {
        let chatBox = document.querySelector(".chat-box")
        chatBox!.scrollTop = chatBox?.scrollHeight as number
    }, 100);
})

const LinkServer = () => {
    // 这里修改你的ip
    ws = new WebSocket("ws://127.0.0.1:1145")
    ws.onopen = () => {
        ChatList.value.push("连接成功")
    }
    ws.onmessage = (data) => {
        let res = JSON.parse(data.data)
        if (res.name == "MyName") {
            myName.value = res.content
        }
        ChatList.value.push(res)
    }
    ws.onclose = () => {
        ChatList.value.push("连接断开，5秒后重连")
        setTimeout(() => {
            ChatList.value.push("连接中...")
            LinkServer()
        }, 5000);
    }
}

LinkServer()
const onSendMsg = () => {
    const msg = {
        msg: sendMsg.value
    }
    ws.send(sendMsg.value)
    sendMsg.value = ""
}

</script>
<template>
    <div id="chat">
        <div class="chat-box">
            <ul>
                <li v-for="(v, k) in ChatList" key="k">
                    <div class="system-message" v-if="(typeof v) == 'string'">
                        {{ v }}
                    </div>

                    <div class="system-message" v-else-if="v.type == 'system'">
                        [{{ v.name }}] {{ v.content }}
                    </div>

                    <div :class="v.name == myName ? 'message self' : ' message '" v-else>
                        <img :src="'https://mc-heads.net/avatar/' + v.name" alt="">
                        <div class="body">
                            <p style="color:#999999;font-weight: bold;">{{ v.name }}</p>
                            <p>
                                {{
                                        v.content
                                }}
                            </p>
                            <small>
                                {{ new Date(v.time).toLocaleString().replace(/年|月/g, "-").replace(/日/g, "") }}
                            </small>
                        </div>
                    </div>

                </li>
            </ul>
        </div>
        <div class="input-box">
            <form @submit.prevent="onSendMsg">

                <input type="text" v-model="sendMsg" />
            </form>
        </div>
    </div>
</template>
<style scoped lang='scss'>
#chat {
    padding: 10px;
    max-width: 800px;
    margin: auto;
}

.chat-box {
    width: 100%;
    height: calc(100vh - 80px);
    // border: 1px solid #000000;
    overflow: auto;
    scroll-behavior: smooth;

    .system-message {
        text-align: center;
    }

    .message {
        display: flex;
        align-items: flex-end;
        max-width: 100%;
        word-break: break-all;

        &.self {
            flex-direction: row-reverse;
            align-items: flex-end;

            .body {
                background-color: #ccf2cf;
            }

            img {
                display: none;
            }
        }

        .body {
            display: flex;
            flex-direction: column;
            margin-left: 8px;
            background-color: #ffffff;
            box-shadow: 2px 1px 3px 1px #00000011;
            padding: 8px;
            border-radius: 8px;



            small {
                font-size: 6px;
            }
        }

        img {
            width: 40px;
            border-radius: 50%;
        }
    }

    ul {
        margin: 0;
        padding: 0;
        list-style: none;

        li {
            margin-bottom: 16px;
            padding: 0 20px;
        }
    }
}

.input-box {
    width: 100%;
    height: 60px;

    form {
        display: flex;
        height: 100%;
    }


    input[type=text] {
        width: 100%;
        border-radius: 25px;
        padding: 0 20px;
        border: 1px solid rgb(159, 159, 159);

        &:focus-visible {
            outline: none;
        }
    }
}
</style>