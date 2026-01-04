# 伤害修正 Mod

当一次攻击已足以决定胜负时，自动补足差值，带来更干净、更流畅的战斗体验。

## 设计目标

有时候你是否蓄力半天放出大招结果还剩一点点血？

有时是否棋差一招被Boss绝地翻盘？

本Mod正是为此而来！

对最终结算的伤害做出判断，当玩家造成的伤害不足以击杀生物但是相差无几时，补足这部分伤害。

战斗，爽！

## 机制

### 计算方式

- `finalDamage`：最终实际伤害（护甲、附魔后）
- `healthBefore`：受击前生命值

```
IF
healthBefore > finalDamage 
AND
healthBefore - finalDamage <= finalDamage * k
```

其中：

`k` 决定了当剩余的血量低于本次造成伤害的多少时，直接补全伤害。

### 举例

- 怪物血量：10
- 本次最终伤害：8

剩余血量：2

默认设定 `k = 0.5`：

`2 <= 8 * 0.5  → 成立`

👉 那就补 2 点伤害，直接击杀

但如果怪物血量是 20：

```
20 - 8 = 12
12 > 8 * 0.5 → 不成立
```

👉 不触发，正常造成伤害

## 可配置项

以下参数可在 config 文件夹下的 damage_correction-common.toml 中修改

```toml
#是否启用伤害修正？
enabled = true
#伤害修正阈值.
#Range: 0.0 ~ 10.0
thresholdRatio = 0.5
#是否对Boss启用？
excludeBoss = true
#自定义boss.
bosses = ["minecraft:ender_dragon", "minecraft:wither", "minecraft:elder_guardian", "minecraft:warden"]
#是否对玩家启用？
excludePlayer = false
#是否只有玩家可以触发伤害修正？
onlyPlayerDamage = true
```

## 许可证

遵循 MIT 开源协议，请随意使用😊
