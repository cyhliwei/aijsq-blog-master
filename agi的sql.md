以下是基于上述功能设计的 MySQL 5.7 SQL 脚本，包含表结构、初始数据和常见操作的示例：

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS wayto_agi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wayto_agi;

-- 用户表（用于记录内容编辑者）
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    password VARCHAR(255) NOT NULL COMMENT '密码哈希',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    role ENUM('admin', 'editor', 'viewer') DEFAULT 'viewer' COMMENT '角色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT '状态',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 菜单项表（存储左侧导航菜单）
CREATE TABLE IF NOT EXISTS menu_items (
    id VARCHAR(50) PRIMARY KEY COMMENT '菜单ID（可自定义，如：1, 2, 16-1）',
    parent_id VARCHAR(50) DEFAULT NULL COMMENT '父菜单ID',
    title VARCHAR(200) NOT NULL COMMENT '菜单标题',
    icon VARCHAR(100) DEFAULT 'el-icon-document' COMMENT '图标类名',
    menu_order INT DEFAULT 0 COMMENT '菜单顺序',
    level INT DEFAULT 1 COMMENT '菜单层级（1：一级，2：二级，3：三级）',
    is_folder TINYINT(1) DEFAULT 0 COMMENT '是否是文件夹（0：否，1：是）',
    is_collapsed TINYINT(1) DEFAULT 0 COMMENT '是否默认折叠（0：否，1：是）',
    is_visible TINYINT(1) DEFAULT 1 COMMENT '是否可见（0：否，1：是）',
    created_by INT COMMENT '创建者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (parent_id) REFERENCES menu_items(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_parent_id (parent_id),
    INDEX idx_menu_order (menu_order),
    INDEX idx_level (level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单项表';

-- 内容表（存储右侧Markdown内容）
CREATE TABLE IF NOT EXISTS contents (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    menu_id VARCHAR(50) NOT NULL COMMENT '关联的菜单ID',
    title VARCHAR(200) NOT NULL COMMENT '内容标题',
    markdown_content LONGTEXT COMMENT 'Markdown格式的内容',
    html_content LONGTEXT COMMENT 'HTML格式的内容（从Markdown转换）',
    views_count INT DEFAULT 0 COMMENT '浏览次数',
    stars_count INT DEFAULT 0 COMMENT '收藏次数',
    likes_count INT DEFAULT 0 COMMENT '点赞次数',
    comments_count INT DEFAULT 0 COMMENT '评论次数',
    last_viewed_at TIMESTAMP NULL COMMENT '最后浏览时间',
    created_by INT COMMENT '创建者ID',
    updated_by INT COMMENT '最后更新者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT DEFAULT 1 COMMENT '版本号',
    status ENUM('draft', 'published', 'archived') DEFAULT 'published' COMMENT '状态',
    FOREIGN KEY (menu_id) REFERENCES menu_items(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_menu_id (menu_id),
    INDEX idx_created_at (created_at),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- 内容历史版本表（用于记录内容的修改历史）
CREATE TABLE IF NOT EXISTS content_versions (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '版本ID',
    content_id INT NOT NULL COMMENT '内容ID',
    version INT NOT NULL COMMENT '版本号',
    title VARCHAR(200) NOT NULL COMMENT '内容标题',
    markdown_content LONGTEXT COMMENT 'Markdown格式的内容',
    created_by INT COMMENT '创建者ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    change_reason VARCHAR(500) COMMENT '修改原因',
    FOREIGN KEY (content_id) REFERENCES contents(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_content_id (content_id),
    INDEX idx_created_at (created_at),
    UNIQUE KEY uk_content_version (content_id, version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容版本历史表';

-- 用户浏览历史表
CREATE TABLE IF NOT EXISTS user_browsing_history (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '历史记录ID',
    user_id INT COMMENT '用户ID（如果已登录）',
    menu_id VARCHAR(50) NOT NULL COMMENT '浏览的菜单ID',
    content_id INT NOT NULL COMMENT '浏览的内容ID',
    session_id VARCHAR(100) COMMENT '会话ID（用于未登录用户）',
    browse_duration INT DEFAULT 0 COMMENT '浏览时长（秒）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (menu_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    FOREIGN KEY (content_id) REFERENCES contents(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_menu_id (menu_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户浏览历史表';

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
    user_id INT NOT NULL COMMENT '用户ID',
    content_id INT NOT NULL COMMENT '内容ID',
    folder_name VARCHAR(100) DEFAULT '默认收藏夹' COMMENT '收藏文件夹名称',
    tags VARCHAR(500) COMMENT '标签（逗号分隔）',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (content_id) REFERENCES contents(id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_content (user_id, content_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    content_id INT NOT NULL COMMENT '内容ID',
    user_id INT COMMENT '用户ID',
    parent_id INT DEFAULT NULL COMMENT '父评论ID（用于回复）',
    username VARCHAR(50) COMMENT '评论者名称（未登录用户）',
    email VARCHAR(100) COMMENT '评论者邮箱（未登录用户）',
    content TEXT NOT NULL COMMENT '评论内容',
    likes_count INT DEFAULT 0 COMMENT '点赞数',
    is_approved TINYINT(1) DEFAULT 1 COMMENT '是否审核通过（0：否，1：是）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (content_id) REFERENCES contents(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (parent_id) REFERENCES comments(id) ON DELETE CASCADE,
    INDEX idx_content_id (content_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 搜索记录表
CREATE TABLE IF NOT EXISTS search_records (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '搜索记录ID',
    user_id INT COMMENT '用户ID',
    keyword VARCHAR(200) NOT NULL COMMENT '搜索关键词',
    menu_id VARCHAR(50) COMMENT '搜索所在的菜单ID',
    result_count INT DEFAULT 0 COMMENT '搜索结果数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (menu_id) REFERENCES menu_items(id) ON DELETE SET NULL,
    INDEX idx_keyword (keyword),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索记录表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS system_configs (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value TEXT COMMENT '配置值',
    config_type ENUM('string', 'int', 'bool', 'json', 'text') DEFAULT 'string' COMMENT '配置类型',
    description VARCHAR(500) COMMENT '配置描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 文件上传记录表
CREATE TABLE IF NOT EXISTS upload_files (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
    user_id INT COMMENT '上传用户ID',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size INT DEFAULT 0 COMMENT '文件大小（字节）',
    file_type VARCHAR(100) COMMENT '文件类型',
    upload_type ENUM('image', 'document', 'video', 'audio', 'other') DEFAULT 'other' COMMENT '上传类型',
    related_content_id INT COMMENT '关联的内容ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (related_content_id) REFERENCES contents(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传记录表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 1. 插入系统管理员用户
INSERT INTO users (username, email, password, role, status) VALUES
('admin', 'admin@waytoagi.com', '$2y$10$YourPasswordHashHere', 'admin', 'active'),
('editor1', 'editor1@waytoagi.com', '$2y$10$YourPasswordHashHere', 'editor', 'active'),
('viewer1', 'viewer1@waytoagi.com', '$2y$10$YourPasswordHashHere', 'viewer', 'active');

-- 2. 插入初始菜单项（按照代码中的结构）
INSERT INTO menu_items (id, parent_id, title, icon, menu_order, level, is_folder) VALUES
-- 顶级菜单项
('1', NULL, '0. 从这里起程', 'el-icon-document', 1, 1, 0),
('2', NULL, 'A1领导力修炼之路', 'el-icon-star-off', 2, 1, 0),
('3', NULL, '社区共创项目及比赛', 'el-icon-trophy', 3, 1, 0),
('4', NULL, 'AI训练营', 'el-icon-reading', 4, 1, 0),
('5', NULL, 'WaytoAGI 高校探索', 'el-icon-school', 5, 1, 0),
('6', NULL, '1.1 入门: AI 学习路径与课程', 'el-icon-notebook-2', 6, 1, 0),
('7', NULL, '1.2 Prompts (提示词)', 'el-icon-edit-outline', 7, 1, 0),
('8', NULL, '1.3 AI Agents (智能体)', 'el-icon-cpu', 8, 1, 0),
('9', NULL, '1.4 AI 绘画', 'el-icon-picture-outline', 9, 1, 0),
('10', NULL, '1.5 AI 视频', 'el-icon-video-camera', 10, 1, 0),
('11', NULL, '1.6 AI 音乐', 'el-icon-headset', 11, 1, 0),
('12', NULL, '1.7 AI 数字人+语音合成', 'el-icon-user', 12, 1, 0),
('13', NULL, '1.8 AI 3D', 'el-icon-box', 13, 1, 0),
('14', NULL, '1.9 ComfyUI 工作流', 'el-icon-connection', 14, 1, 0),
('15', NULL, '2.0 AI 编程', 'el-icon-document-copy', 15, 1, 0),
('16', NULL, '2.1 喂起码: 个人专栏', 'el-icon-folder-opened', 16, 1, 1),

-- 个人专栏的子菜单
('16-1', '16', '提示词目录', 'el-icon-tickets', 1, 2, 0),
('16-2', '16', '小土堆-提示词', 'el-icon-document', 2, 2, 0),
('16-3', '16', '蓝衣侠者-AIGC提炼火花', 'el-icon-document', 3, 2, 0),
('16-4', '16', '云中江树- LangGPT 提示词', 'el-icon-document', 4, 2, 0),
('16-5', '16', '周慎谦士-提示词', 'el-icon-document', 5, 2, 0),
('16-6', '16', '繁鹤居-提示词', 'el-icon-document', 6, 2, 0);

-- 3. 插入初始内容
INSERT INTO contents (menu_id, title, markdown_content, views_count, stars_count, likes_count, comments_count, created_by, status) VALUES
('1', '0. 从这里起程', '# 从这里起程\n\n欢迎来到通往AGI之路！这是你的起点。\n\n## 学习路线\n\n1. **基础知识**：了解AI的基本概念\n2. **实践项目**：通过动手实践加深理解\n3. **社区交流**：与其他学习者互动\n\n## 推荐资源\n\n- [AI学习路线图](https://example.com)\n- [实战项目合集](https://example.com/projects)\n- [常见问题解答](https://example.com/faq)', 1245, 567, 234, 89, 1, 'published'),
('2', 'A1领导力修炼之路', '# A1领导力修炼之路\n\n学习如何成为AI时代的领导者。\n\n## 领导力要素\n\n### 技术理解力\n- 了解AI技术的基本原理\n- 能够与技术团队有效沟通\n- 识别技术趋势和机会\n\n### 战略思维\n- 制定AI战略规划\n- 评估AI项目的商业价值\n- 管理AI项目的风险\n\n### 团队管理\n- 建立高效AI团队\n- 培养AI人才\n- 促进团队协作和创新', 3567, 1234, 456, 123, 1, 'published'),
('16-1', '提示词目录', '# 提示词目录\n\n所有提示词的索引。\n\n## 分类\n\n### 基础提示词\n- **写作助手**：帮助写作各种文档\n- **代码助手**：编程相关的提示词\n- **学习助手**：学习规划的提示词\n\n### 进阶提示词\n- **创意生成**：创意写作和构思\n- **专业领域**：特定领域的提示词\n- **系统提示**：系统级别的提示词\n\n### 专家提示词\n- **高级编程**：复杂编程任务的提示词\n- **学术研究**：学术写作和研究的提示词\n- **商业应用**：商业场景的提示词', 8900, 3456, 789, 234, 1, 'published');

-- 4. 插入系统配置
INSERT INTO system_configs (config_key, config_value, config_type, description) VALUES
('site_title', '通往AGI之路', 'string', '网站标题'),
('site_description', '专注于人工智能学习的知识库与社区平台', 'string', '网站描述'),
('site_keywords', 'AI,人工智能,AGI,学习路径,知识库', 'string', '网站关键词'),
('default_theme', 'light', 'string', '默认主题'),
('editor_toolbar_config', '{"bold":true,"italic":true,"header":true,"underline":true,"strikethrough":true,"mark":true,"superscript":true,"subscript":true,"quote":true,"ol":true,"ul":true,"link":true,"imagelink":true,"code":true,"table":true,"fullscreen":true,"readmodel":true,"htmlcode":true,"help":true,"undo":true,"redo":true,"trash":true,"navigation":true,"subfield":true,"preview":true}', 'json', '编辑器工具栏配置'),
('sidebar_default_collapsed', '0', 'bool', '侧边栏默认是否折叠'),
('enable_comments', '1', 'bool', '是否启用评论功能'),
('enable_search_history', '1', 'bool', '是否启用搜索历史记录');

-- ============================================
-- 创建存储过程（可选）
-- ============================================

-- 获取菜单树形结构的存储过程
DELIMITER //
CREATE PROCEDURE get_menu_tree()
BEGIN
    SELECT 
        m1.*,
        COUNT(m2.id) AS has_children
    FROM menu_items m1
    LEFT JOIN menu_items m2 ON m1.id = m2.parent_id
    GROUP BY m1.id
    ORDER BY m1.level, m1.menu_order;
END //
DELIMITER ;

-- 更新内容浏览次数的存储过程
DELIMITER //
CREATE PROCEDURE update_content_view_count(IN p_content_id INT)
BEGIN
    UPDATE contents 
    SET 
        views_count = views_count + 1,
        last_viewed_at = NOW()
    WHERE id = p_content_id;
END //
DELIMITER ;

-- 获取热门内容的存储过程
DELIMITER //
CREATE PROCEDURE get_popular_contents(IN p_limit INT)
BEGIN
    SELECT 
        c.*,
        m.title AS menu_title
    FROM contents c
    JOIN menu_items m ON c.menu_id = m.id
    WHERE c.status = 'published'
    ORDER BY c.views_count DESC, c.updated_at DESC
    LIMIT p_limit;
END //
DELIMITER ;

-- ============================================
-- 创建触发器
-- ============================================

-- 当内容更新时，自动保存到历史版本表
DELIMITER //
CREATE TRIGGER before_content_update
BEFORE UPDATE ON contents
FOR EACH ROW
BEGIN
    -- 如果内容有变化，保存到历史版本表
    IF OLD.markdown_content <> NEW.markdown_content OR OLD.title <> NEW.title THEN
        INSERT INTO content_versions (content_id, version, title, markdown_content, created_by, change_reason)
        VALUES (OLD.id, OLD.version, OLD.title, OLD.markdown_content, OLD.updated_by, '自动保存历史版本');
        
        -- 递增版本号
        SET NEW.version = OLD.version + 1;
    END IF;
END //
DELIMITER ;

-- ============================================
-- 创建视图
-- ============================================

-- 菜单项详情视图（包含内容统计）
CREATE VIEW menu_items_detail AS
SELECT 
    m.*,
    c.title AS content_title,
    c.views_count,
    c.stars_count,
    c.likes_count,
    c.comments_count,
    c.updated_at AS content_updated_at,
    u.username AS creator_name
FROM menu_items m
LEFT JOIN contents c ON m.id = c.menu_id AND c.status = 'published'
LEFT JOIN users u ON m.created_by = u.id;

-- 内容详情视图（包含菜单信息）
CREATE VIEW contents_detail AS
SELECT 
    c.*,
    m.title AS menu_title,
    m.icon AS menu_icon,
    m.parent_id AS menu_parent_id,
    u1.username AS creator_name,
    u2.username AS updater_name
FROM contents c
JOIN menu_items m ON c.menu_id = m.id
LEFT JOIN users u1 ON c.created_by = u1.id
LEFT JOIN users u2 ON c.updated_by = u2.id;

-- 用户活动统计视图
CREATE VIEW user_activity_stats AS
SELECT 
    u.id,
    u.username,
    u.email,
    u.role,
    u.created_at AS user_created_at,
    COUNT(DISTINCT c.id) AS content_count,
    COUNT(DISTINCT f.id) AS favorite_count,
    COUNT(DISTINCT cm.id) AS comment_count,
    COUNT(DISTINCT h.id) AS browse_history_count,
    MAX(c.updated_at) AS last_activity_time
FROM users u
LEFT JOIN contents c ON u.id = c.created_by
LEFT JOIN favorites f ON u.id = f.user_id
LEFT JOIN comments cm ON u.id = cm.user_id
LEFT JOIN user_browsing_history h ON u.id = h.user_id
GROUP BY u.id;

-- ============================================
-- 创建索引优化查询性能
-- ============================================

-- 为菜单项添加全文索引（用于搜索）
ALTER TABLE menu_items ADD FULLTEXT INDEX idx_ft_title (title);

-- 为内容表添加全文索引（用于搜索）
ALTER TABLE contents ADD FULLTEXT INDEX idx_ft_title_content (title, markdown_content(500));

-- 为评论表添加全文索引
ALTER TABLE comments ADD FULLTEXT INDEX idx_ft_content (content);

-- ============================================
-- 示例查询
-- ============================================

-- 1. 查询所有菜单及其子菜单数量
SELECT 
    m1.id,
    m1.title,
    m1.icon,
    m1.level,
    COUNT(m2.id) AS child_count
FROM menu_items m1
LEFT JOIN menu_items m2 ON m1.id = m2.parent_id
GROUP BY m1.id, m1.title, m1.icon, m1.level
ORDER BY m1.level, m1.menu_order;

-- 2. 查询最近更新的内容
SELECT 
    c.id,
    c.title,
    m.title AS menu_title,
    c.views_count,
    c.updated_at,
    u.username AS updater_name
FROM contents c
JOIN menu_items m ON c.menu_id = m.id
LEFT JOIN users u ON c.updated_by = u.id
WHERE c.status = 'published'
ORDER BY c.updated_at DESC
LIMIT 10;

-- 3. 查询热门内容（按浏览量）
SELECT 
    c.id,
    c.title,
    m.title AS menu_title,
    c.views_count,
    c.stars_count,
    c.likes_count,
    ROUND(c.views_count * 0.3 + c.stars_count * 0.4 + c.likes_count * 0.3, 2) AS hot_score
FROM contents c
JOIN menu_items m ON c.menu_id = m.id
WHERE c.status = 'published'
ORDER BY hot_score DESC
LIMIT 10;

-- 4. 查询用户的浏览历史
SELECT 
    h.id,
    h.created_at AS browse_time,
    m.title AS menu_title,
    c.title AS content_title,
    h.browse_duration
FROM user_browsing_history h
JOIN menu_items m ON h.menu_id = m.id
JOIN contents c ON h.content_id = c.id
WHERE h.user_id = 1 -- 替换为具体用户ID
ORDER BY h.created_at DESC
LIMIT 20;

-- 5. 搜索功能示例
SELECT 
    c.id AS content_id,
    c.title AS content_title,
    c.markdown_content,
    m.title AS menu_title,
    MATCH(c.title, c.markdown_content) AGAINST('AI 学习' IN NATURAL LANGUAGE MODE) AS relevance_score
FROM contents c
JOIN menu_items m ON c.menu_id = m.id
WHERE MATCH(c.title, c.markdown_content) AGAINST('AI 学习' IN NATURAL LANGUAGE MODE)
AND c.status = 'published'
ORDER BY relevance_score DESC
LIMIT 20;

-- ============================================
-- 数据库用户和权限设置（示例）
-- ============================================

-- 创建只读用户
-- CREATE USER 'readonly_user'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT SELECT ON wayto_agi.* TO 'readonly_user'@'localhost';

-- 创建读写用户
-- CREATE USER 'readwrite_user'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT SELECT, INSERT, UPDATE ON wayto_agi.* TO 'readwrite_user'@'localhost';

-- 创建管理员用户
-- CREATE USER 'admin_user'@'localhost' IDENTIFIED BY 'your_password';
-- GRANT ALL PRIVILEGES ON wayto_agi.* TO 'admin_user'@'localhost';

-- 刷新权限
-- FLUSH PRIVILEGES;

-- ============================================
-- 数据库维护脚本
-- ============================================

-- 1. 清理过期的浏览历史（保留30天）
-- DELETE FROM user_browsing_history WHERE created_at < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 2. 清理过期的搜索记录（保留7天）
-- DELETE FROM search_records WHERE created_at < DATE_SUB(NOW(), INTERVAL 7 DAY);

-- 3. 更新内容统计信息（每天运行）
-- UPDATE contents c
-- SET 
--     views_count = (SELECT COUNT(*) FROM user_browsing_history WHERE content_id = c.id),
--     likes_count = (SELECT COUNT(*) FROM comments WHERE content_id = c.id AND is_approved = 1)
-- WHERE c.status = 'published';

-- ============================================
-- 导出数据库结构
-- ============================================

-- 导出数据库结构（不包含数据）
-- mysqldump -u username -p --no-data wayto_agi > wayto_agi_structure.sql

-- 导出数据库结构和数据
-- mysqldump -u username -p wayto_agi > wayto_agi_full.sql
```

## 主要特点：

### 1. **表结构设计**
- **menu_items**: 存储左侧导航菜单，支持无限级嵌套
- **contents**: 存储右侧Markdown内容，与菜单项关联
- **content_versions**: 内容版本控制，记录修改历史
- **users**: 用户管理，支持角色权限
- **user_browsing_history**: 用户浏览历史记录
- **favorites**: 收藏功能
- **comments**: 评论系统
- **search_records**: 搜索历史记录

### 2. **核心功能支持**
- 菜单的增删改查（但没有删除功能，符合要求）
- 内容的版本控制
- 用户权限管理
- 浏览统计和热门内容分析
- 搜索功能优化

### 3. **性能优化**
- 适当的索引设计
- 全文索引支持搜索
- 视图简化复杂查询
- 存储过程和触发器自动化任务

### 4. **数据完整性**
- 外键约束确保数据一致性
- 级联删除和更新设置
- 软删除标志（is_visible）而非物理删除

### 5. **扩展性**
- 支持文件上传管理
- 系统配置表便于定制
- 用户活动统计视图
- 可扩展的角色权限系统

## 使用方法：

1. 创建数据库：
```bash
mysql -u root -p < wayto_agi.sql
```

2. 或者逐条执行SQL语句

3. 根据需要修改初始数据（用户密码、内容等）

4. 定期运行维护脚本清理过期数据

这个SQL脚本完全支持前端Vue.js应用的所有功能，包括菜单管理、内容编辑、用户交互等，并且考虑了性能、扩展性和数据完整性。