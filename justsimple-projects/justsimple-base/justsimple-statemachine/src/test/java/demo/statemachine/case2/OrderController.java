package demo.statemachine.case2;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Post;
import org.mutantcat.justsimple.core.handle.Result;
import org.mutantcat.justsimple.statemachine.EventContext;

/**
 *
 * @author noear 2025/8/23 created
 *
 */
@Controller
public class OrderController {
    @Inject
    private OrderStateMachine orderStatusMachine;

    @Mapping("/create")
    public Result create() {
        // TODO 模拟订单落库
        return Result.succeed();
    }

    @Post
    @Mapping("/pay")
    public Result pay() {
        // TODO 模拟订单支付
        return Result.succeed();
    }

    @Post
    @Mapping("/payNotify")
    public Result payNotify(Integer orderId) {
        // TODO 支付成功后，使用状态机

        // 模拟通过id查找一个订单对象
        OrderEntity entity = new OrderEntity(orderId, OrderStatusEnum.WAIT_PAY);

        // 使用状态机发送这个消息
        orderStatusMachine.sendEvent(OrderStatusEventEnum.PAY, EventContext.of(entity.getStatus(), entity));
        return Result.succeed();
    }
}
